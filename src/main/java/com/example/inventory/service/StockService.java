package com.example.inventory.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.inventory.dto.StockListDto;
import com.example.inventory.entity.Product;
import com.example.inventory.entity.Stock;
import com.example.inventory.entity.Warehouse;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.form.StockForm;
import com.example.inventory.form.StockSearchForm;
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
        return stockRepository.findAllByOrderByStockIdAsc();
    }

    @Transactional
    public void save(StockForm form) {
        if (stockRepository.existsByProductProductIdAndWarehouseWarehouseId(
                form.getProductId(), form.getWarehouseId())) {
            throw new BusinessException("同じ商品・同じ倉庫の在庫が既に登録されています。");
        }

        Product product = productRepository.findById(form.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("商品が見つかりません。"));
        Warehouse warehouse = warehouseRepository.findById(form.getWarehouseId())
            .orElseThrow(() -> new ResourceNotFoundException("倉庫が見つかりません。"));

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setWarehouse(warehouse);
        stock.setQuantity(0);

        stockRepository.save(stock);
    }

    @Transactional(readOnly = true)
    public Stock findById(Integer stockId) {
        return stockRepository.findById(stockId)
            .orElseThrow(() -> new ResourceNotFoundException("在庫が見つかりません。"));
    }

    @Transactional
    public void update(StockForm form) {
        if (stockRepository.existsByProductProductIdAndWarehouseWarehouseIdAndStockIdNot(
                form.getProductId(), form.getWarehouseId(), form.getStockId())) {
            throw new BusinessException("同じ商品・同じ倉庫の在庫が既に登録されています。");
        }

        Stock stock = stockRepository.findById(form.getStockId())
            .orElseThrow(() -> new ResourceNotFoundException("在庫が見つかりません。"));
        Product product = productRepository.findById(form.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("商品が見つかりません。"));
        Warehouse warehouse = warehouseRepository.findById(form.getWarehouseId())
            .orElseThrow(() -> new ResourceNotFoundException("倉庫が見つかりません。"));

        stock.setProduct(product);
        stock.setWarehouse(warehouse);

        stockRepository.save(stock);
    }

    @Transactional
    public void delete(Integer stockId) {
        stockRepository.deleteById(stockId);
    }

    // @Transactional(readOnly = true)
    // public List<StockListDto> findAllForList() {
    //     return stockRepository.findAllByOrderByStockIdAsc()
    //             .stream()
    //             .map(this::toStockListDto)
    //             .collect(Collectors.toList());
    // }

    private StockListDto toStockListDto(Stock stock) {
        StockListDto dto = new StockListDto();
        dto.setStockId(stock.getStockId());
        dto.setProductId(stock.getProduct().getProductId());
        dto.setProductName(stock.getProduct().getProductName());
        dto.setWarehouseId(stock.getWarehouse().getWarehouseId());
        dto.setWarehouseName(stock.getWarehouse().getWarehouseName());
        dto.setQuantity(stock.getQuantity());
        dto.setCreatedAt(stock.getCreatedAt());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<StockListDto> searchForList(StockSearchForm form) {
        String productName = "";
        String warehouseName = "";

        if (form != null) {
            if (StringUtils.hasText(form.getProductName())) {
                productName = form.getProductName().trim();
            }
            if (StringUtils.hasText(form.getWarehouseName())) {
                warehouseName = form.getWarehouseName().trim();
            }
        }

        return stockRepository
                .findByProductProductNameContainingAndWarehouseWarehouseNameContainingOrderByStockIdAsc(
                        productName, warehouseName)
                .stream()
                .map(this::toStockListDto)
                .toList();
    }
}