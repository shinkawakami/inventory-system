package com.example.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.entity.Warehouse;
import com.example.inventory.form.WarehouseForm;
import com.example.inventory.repository.WarehouseRepository;

@Service
public class WarehouseService {

    private final WarehouseRepository repository;

    public WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Warehouse> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void save(WarehouseForm form) {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseName(form.getWarehouseName());
        warehouse.setLocation(form.getLocation());
        repository.save(warehouse);
    }
}