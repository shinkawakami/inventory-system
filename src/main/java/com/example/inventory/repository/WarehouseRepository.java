package com.example.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    List<Warehouse> findAllByOrderByWarehouseIdAsc();

    List<Warehouse> findByWarehouseNameContainingOrderByWarehouseIdAsc(String warehouseName);
}