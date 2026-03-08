package com.example.inventory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    List<Warehouse> findAllByOrderByWarehouseIdAsc();

    Page<Warehouse> findAllByOrderByWarehouseIdAsc(Pageable pageable);

    Page<Warehouse> findByWarehouseNameContainingOrderByWarehouseIdAsc(String warehouseName, Pageable pageable);
}