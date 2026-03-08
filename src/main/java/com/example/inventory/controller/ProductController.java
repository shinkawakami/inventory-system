package com.example.inventory.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.inventory.entity.Product;
import com.example.inventory.form.ProductForm;
import com.example.inventory.form.ProductSearchForm;
import com.example.inventory.service.CsvExportService;
import com.example.inventory.service.ProductService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService service;
    private final CsvExportService csvExportService;

    public ProductController(ProductService service, CsvExportService csvExportService) {
        this.service = service;
        this.csvExportService = csvExportService;
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
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "product/regist";
        }

        service.save(form);
        redirectAttributes.addFlashAttribute("successMessage", "商品を登録しました。");
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
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "product/edit";
        }

        service.update(form);
        redirectAttributes.addFlashAttribute("successMessage", "商品を更新しました。");
        return "redirect:/product/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("productId") Integer productId,
            RedirectAttributes redirectAttributes) {
        service.delete(productId);
        redirectAttributes.addFlashAttribute("successMessage", "商品を削除しました。");
        return "redirect:/product/list";
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportCsv(@ModelAttribute ProductSearchForm form) {
        List<Product> products = service.searchAll(form);

        String[] headers = {"ID", "商品名", "価格", "登録日時"};
        List<String[]> rows = products.stream()
                .map(p -> new String[]{
                        String.valueOf(p.getProductId()),
                        p.getProductName(),
                        String.valueOf(p.getPrice()),
                        p.getCreatedAt() != null ? p.getCreatedAt().toString() : ""
                })
                .toList();

        byte[] csv = csvExportService.export(headers, rows);

        String filename = "products_" + LocalDate.now() + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename, StandardCharsets.UTF_8).build().toString())
                .contentType(new MediaType("text", "csv"))
                .body(csv);
    }
}