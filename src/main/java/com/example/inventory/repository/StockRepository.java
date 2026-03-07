package com.example.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    boolean existsByProductProductIdAndWarehouseWarehouseId(Integer productId, Integer warehouseId);

    boolean existsByProductProductIdAndWarehouseWarehouseIdAndStockIdNot(
            Integer productId,
            Integer warehouseId,
            Integer stockId
    );
}