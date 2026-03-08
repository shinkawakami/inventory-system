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

import com.example.inventory.dto.StockListDto;
import com.example.inventory.entity.Stock;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.form.StockForm;
import com.example.inventory.form.StockSearchForm;
import com.example.inventory.service.CsvExportService;
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
    private final CsvExportService csvExportService;

    public StockController(StockService stockService,
                        ProductService productService,
                        WarehouseService warehouseService,
                        CsvExportService csvExportService) {
        this.stockService = stockService;
        this.productService = productService;
        this.warehouseService = warehouseService;
        this.csvExportService = csvExportService;
    }

    @GetMapping("/list")
    public String list(@ModelAttribute("searchForm") StockSearchForm form,
                    @RequestParam(name = "page", defaultValue = "0") int page,
                    @RequestParam(name = "size", defaultValue = "10") int size,
                    Model model) {

        Page<StockListDto> stockPage = stockService.searchForList(form, page, size);

        model.addAttribute("stocks", stockPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", stockPage.getTotalPages());
        model.addAttribute("size", size);

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

        try {
            stockService.save(form);
        } catch (BusinessException e) {
            model.addAttribute("products", productService.findAll());
            model.addAttribute("warehouses", warehouseService.findAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "stock/regist";
        }

        return "redirect:/stock/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, Model model) {
        Stock stock = stockService.findById(id);

        StockForm form = new StockForm();
        form.setStockId(stock.getStockId());
        form.setProductId(stock.getProduct().getProductId());
        form.setWarehouseId(stock.getWarehouse().getWarehouseId());

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

        try {
            stockService.update(form);
        } catch (BusinessException e) {
            model.addAttribute("products", productService.findAll());
            model.addAttribute("warehouses", warehouseService.findAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "stock/edit";
        }

        return "redirect:/stock/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("stockId") Integer stockId) {
        stockService.delete(stockId);
        return "redirect:/stock/list";
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportCsv(@ModelAttribute StockSearchForm form) {
        List<StockListDto> stocks = stockService.searchAllForList(form);

        String[] headers = {"ID", "商品名", "倉庫名", "在庫数", "登録日時"};
        List<String[]> rows = stocks.stream()
                .map(s -> new String[]{
                        String.valueOf(s.getStockId()),
                        s.getProductName(),
                        s.getWarehouseName(),
                        String.valueOf(s.getQuantity()),
                        s.getCreatedAt() != null ? s.getCreatedAt().toString() : ""
                })
                .toList();

        byte[] csv = csvExportService.export(headers, rows);

        String filename = "stocks_" + LocalDate.now() + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename, StandardCharsets.UTF_8).build().toString())
                .contentType(new MediaType("text", "csv"))
                .body(csv);
    }
}