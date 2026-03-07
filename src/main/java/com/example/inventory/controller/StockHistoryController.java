package com.example.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.inventory.exception.BusinessException;
import com.example.inventory.form.StockHistoryForm;
import com.example.inventory.service.StockHistoryService;
import com.example.inventory.service.StockService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/stock-history")
public class StockHistoryController {

    private final StockHistoryService stockHistoryService;
    private final StockService stockService;

    public StockHistoryController(StockHistoryService stockHistoryService,
                                  StockService stockService) {
        this.stockHistoryService = stockHistoryService;
        this.stockService = stockService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("histories", stockHistoryService.findAll());
        return "stock_history/list";
    }

    @GetMapping("/regist")
    public String registForm(Model model) {
        model.addAttribute("stockHistoryForm", new StockHistoryForm());
        model.addAttribute("stocks", stockService.findAll());
        return "stock_history/regist";
    }

    @PostMapping("/regist")
    public String regist(@ModelAttribute("stockHistoryForm") @Valid StockHistoryForm form,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("stocks", stockService.findAll());
            return "stock_history/regist";
        }

        try {
            stockHistoryService.save(form);
        } catch (BusinessException e) {
            model.addAttribute("stocks", stockService.findAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "stock_history/regist";
        }
        
        return "redirect:/stock-history/list";
    }
}