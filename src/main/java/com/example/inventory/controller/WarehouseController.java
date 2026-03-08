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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.inventory.entity.Warehouse;
import com.example.inventory.form.WarehouseForm;
import com.example.inventory.form.WarehouseSearchForm;
import com.example.inventory.service.CsvExportService;
import com.example.inventory.service.WarehouseService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    private final WarehouseService service;
    private final CsvExportService csvExportService;

    public WarehouseController(WarehouseService service, CsvExportService csvExportService) {
        this.service = service;
        this.csvExportService = csvExportService;
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

    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportCsv(@ModelAttribute WarehouseSearchForm form) {
        List<Warehouse> warehouses = service.searchAll(form);

        String[] headers = {"ID", "倉庫名", "所在地", "登録日時"};
        List<String[]> rows = warehouses.stream()
                .map(w -> new String[]{
                        String.valueOf(w.getWarehouseId()),
                        w.getWarehouseName(),
                        w.getLocation(),
                        w.getCreatedAt() != null ? w.getCreatedAt().toString() : ""
                })
                .toList();

        byte[] csv = csvExportService.export(headers, rows);

        String filename = "warehouses_" + LocalDate.now() + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename, StandardCharsets.UTF_8).build().toString())
                .contentType(new MediaType("text", "csv"))
                .body(csv);
    }
}