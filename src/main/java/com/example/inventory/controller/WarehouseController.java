package com.example.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.inventory.entity.Warehouse;
import com.example.inventory.form.WarehouseForm;
import com.example.inventory.form.WarehouseSearchForm;
import com.example.inventory.service.WarehouseService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    private final WarehouseService service;

    public WarehouseController(WarehouseService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public String list(@ModelAttribute("searchForm") WarehouseSearchForm form,
                    @RequestParam(name = "page", defaultValue = "0") int page,
                    @RequestParam(name = "size", defaultValue = "10") int size,
                    Model model) {

        Page<Warehouse> warehousePage = service.search(form, page, size);

        model.addAttribute("warehouses", warehousePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", warehousePage.getTotalPages());
        model.addAttribute("size", size);

        return "warehouse/list";
    }

    @GetMapping("/regist")
    public String registForm(Model model) {
        model.addAttribute("warehouseForm", new WarehouseForm());
        return "warehouse/regist";
    }

    @PostMapping("/regist")
    public String regist(@ModelAttribute("warehouseForm") @Valid WarehouseForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "warehouse/regist";
        }

        service.save(form);
        return "redirect:/warehouse/list";
    }
}