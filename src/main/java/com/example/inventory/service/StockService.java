package com.example.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.entity.Product;
import com.example.inventory.entity.Stock;
import com.example.inventory.entity.Warehouse;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.form.StockForm;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.StockRepository;
import com.example.inventory.repository.WarehouseRepository;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    public StockService(StockRepository stockRepository,
                        ProductRepository productRepository,
                        WarehouseRepository warehouseRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Transactional(readOnly = true)
    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    @Transactional
    public void save(StockForm form) {
        if (stockRepository.existsByProductProductIdAndWarehouseWarehouseId(
                form.getProductId(), form.getWarehouseId())) {
            throw new BusinessException("同じ商品・同じ倉庫の在庫が既に登録されています。");
        }

        Product product = productRepository.findById(form.getProductId()).orElseThrow();
        Warehouse warehouse = warehouseRepository.findById(form.getWarehouseId()).orElseThrow();

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setWarehouse(warehouse);
        stock.setQuantity(0);

        stockRepository.save(stock);
    }

    @Transactional(readOnly = true)
    public Stock findById(Integer stockId) {
        return stockRepository.findById(stockId).orElse(null);
    }

    @Transactional
    public void update(StockForm form) {
        if (stockRepository.existsByProductProductIdAndWarehouseWarehouseIdAndStockIdNot(
                form.getProductId(), form.getWarehouseId(), form.getStockId())) {
            throw new BusinessException("同じ商品・同じ倉庫の在庫が既に登録されています。");
        }

        Stock stock = stockRepository.findById(form.getStockId()).orElseThrow();
        Product product = productRepository.findById(form.getProductId()).orElseThrow();
        Warehouse warehouse = warehouseRepository.findById(form.getWarehouseId()).orElseThrow();

        stock.setProduct(product);
        stock.setWarehouse(warehouse);

        stockRepository.save(stock);
    }

    @Transactional
    public void delete(Integer stockId) {
        stockRepository.deleteById(stockId);
    }
}