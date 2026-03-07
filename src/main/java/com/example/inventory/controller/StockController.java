package com.example.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.inventory.entity.Stock;
import com.example.inventory.form.StockForm;
import com.example.inventory.service.ProductService;
import com.example.inventory.service.StockService;
import com.example.inventory.service.WarehouseService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;
    private final ProductService productService;
    private final WarehouseService warehouseService;

    public StockController(StockService stockService,
                           ProductService productService,
                           WarehouseService warehouseService) {
        this.stockService = stockService;
        this.productService = productService;
        this.warehouseService = warehouseService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("stocks", stockService.findAll());
        return "stock/list";
    }

    @GetMapping("/regist")
    public String registForm(Model model) {
        model.addAttribute("stockForm", new StockForm());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("warehouses", warehouseService.findAll());
        return "stock/regist";
    }

    @PostMapping("/regist")
    public String regist(@ModelAttribute("stockForm") @Valid StockForm form,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("products", productService.findAll());
            model.addAttribute("warehouses", warehouseService.findAll());
            return "stock/regist";
        }

        stockService.save(form);
        return "redirect:/stock/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        Stock stock = stockService.findById(id);

        StockForm form = new StockForm();
        form.setStockId(stock.getStockId());
        form.setProductId(stock.getProduct().getProductId());
        form.setWarehouseId(stock.getWarehouse().getWarehouseId());
        form.setQuantity(stock.getQuantity());

        model.addAttribute("stockForm", form);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("warehouses", warehouseService.findAll());

        return "stock/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("stockForm") @Valid StockForm form,
                       BindingResult bindingResult,
                       Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("products", productService.findAll());
            model.addAttribute("warehouses", warehouseService.findAll());
            return "stock/edit";
        }

        stockService.update(form);
        return "redirect:/stock/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("stockId") Integer stockId) {
        stockService.delete(stockId);
        return "redirect:/stock/list";
    }
}