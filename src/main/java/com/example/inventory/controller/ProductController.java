package com.example.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.inventory.entity.Product;
import com.example.inventory.form.ProductForm;
import com.example.inventory.form.ProductSearchForm;
import com.example.inventory.service.ProductService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public String list(@ModelAttribute("searchForm") ProductSearchForm form,
                    @RequestParam(name = "page", defaultValue = "0") int page,
                    @RequestParam(name = "size", defaultValue = "10") int size,
                    Model model) {

        Page<Product> productPage = service.search(form, page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("size", size);

        return "product/list";
    }

    @GetMapping("/regist")
    public String registForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "product/regist";
    }

    @PostMapping("/regist")
    public String regist(@ModelAttribute("productForm") @Valid ProductForm form,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product/regist";
        }
        service.save(form);
        return "redirect:/product/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        Product product = service.findById(id);

        ProductForm form = new ProductForm();
        form.setProductId(product.getProductId());
        form.setProductName(product.getProductName());
        form.setPrice(product.getPrice());

        model.addAttribute("productForm", form);
        return "product/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("productForm") @Valid ProductForm form,
                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product/edit";
        }
        service.update(form);
        return "redirect:/product/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("productId") Integer productId) {
        service.delete(productId);
        return "redirect:/product/list";
    }
}