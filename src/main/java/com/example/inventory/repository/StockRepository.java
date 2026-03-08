package com.example.inventory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    List<Stock> findAllByOrderByStockIdAsc();

    List<Stock> findByProductProductNameContainingAndWarehouseWarehouseNameContainingOrderByStockIdAsc(
            String productName,
            String warehouseName
    );

    Page<Stock> findAllByOrderByStockIdAsc(Pageable pageable);

    Page<Stock> findByProductProductNameContainingAndWarehouseWarehouseNameContainingOrderByStockIdAsc(
            String productName,
            String warehouseName,
            Pageable pageable
    );

    List<Stock> findTop5ByQuantityLessThanEqualOrderByQuantityAscStockIdAsc(Integer quantity);
}