package com.example.inventory.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.inventory.entity.Warehouse;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.form.WarehouseForm;
import com.example.inventory.form.WarehouseSearchForm;
import com.example.inventory.repository.WarehouseRepository;

@Service
public class WarehouseService {

    private final WarehouseRepository repository;

    public WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Warehouse> findAll() {
        return repository.findAllByOrderByWarehouseIdAsc();
    }

    @Transactional
    public void save(WarehouseForm form) {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseName(form.getWarehouseName());
        warehouse.setLocation(form.getLocation());
        repository.save(warehouse);
    }

    @Transactional(readOnly = true)
    public Warehouse findById(Integer warehouseId) {
        return repository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("倉庫が見つかりません。"));
    }

    @Transactional(readOnly = true)
    public Page<Warehouse> search(WarehouseSearchForm form, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (form == null || !StringUtils.hasText(form.getWarehouseName())) {
            return repository.findAllByOrderByWarehouseIdAsc(pageable);
        }

        return repository.findByWarehouseNameContainingOrderByWarehouseIdAsc(
                form.getWarehouseName().trim(),
                pageable
        );
    }

    @Transactional(readOnly = true)
    public List<Warehouse> searchAll(WarehouseSearchForm form) {
        if (form == null || !org.springframework.util.StringUtils.hasText(form.getWarehouseName())) {
            return repository.findAllByOrderByWarehouseIdAsc();
        }

        return repository.findByWarehouseNameContainingOrderByWarehouseIdAsc(form.getWarehouseName().trim());
    }
}