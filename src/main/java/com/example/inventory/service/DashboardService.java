package com.example.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.dto.DashboardDto;
import com.example.inventory.dto.LowStockDto;
import com.example.inventory.dto.RecentStockHistoryDto;
import com.example.inventory.entity.Stock;
import com.example.inventory.entity.StockHistory;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.StockHistoryRepository;
import com.example.inventory.repository.StockRepository;
import com.example.inventory.repository.WarehouseRepository;

@Service
public class DashboardService {

    private static final int LOW_STOCK_THRESHOLD = 5;

    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final StockRepository stockRepository;
    private final StockHistoryRepository stockHistoryRepository;

    public DashboardService(ProductRepository productRepository,
                            WarehouseRepository warehouseRepository,
                            StockRepository stockRepository,
                            StockHistoryRepository stockHistoryRepository) {
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.stockRepository = stockRepository;
        this.stockHistoryRepository = stockHistoryRepository;
    }

    @Transactional(readOnly = true)
    public DashboardDto getDashboard() {
        DashboardDto dto = new DashboardDto();
        dto.setProductCount(productRepository.count());
        dto.setWarehouseCount(warehouseRepository.count());
        dto.setStockCount(stockRepository.count());
        dto.setStockHistoryCount(stockHistoryRepository.count());
        dto.setLowStocks(getLowStocks());
        dto.setRecentHistories(getRecentHistories());
        return dto;
    }

    private List<LowStockDto> getLowStocks() {
        return stockRepository
                .findTop5ByQuantityLessThanEqualOrderByQuantityAscStockIdAsc(LOW_STOCK_THRESHOLD)
                .stream()
                .map(this::toLowStockDto)
                .toList();
    }

    private List<RecentStockHistoryDto> getRecentHistories() {
        return stockHistoryRepository
                .findTop5ByOrderByCreatedAtDescHistoryIdDesc()
                .stream()
                .map(this::toRecentStockHistoryDto)
                .toList();
    }

    private LowStockDto toLowStockDto(Stock stock) {
        LowStockDto dto = new LowStockDto();
        dto.setStockId(stock.getStockId());
        dto.setProductName(stock.getProduct().getProductName());
        dto.setWarehouseName(stock.getWarehouse().getWarehouseName());
        dto.setQuantity(stock.getQuantity());
        return dto;
    }

    private RecentStockHistoryDto toRecentStockHistoryDto(StockHistory history) {
        RecentStockHistoryDto dto = new RecentStockHistoryDto();
        dto.setHistoryId(history.getHistoryId());
        dto.setProductName(history.getStock().getProduct().getProductName());
        dto.setWarehouseName(history.getStock().getWarehouse().getWarehouseName());
        dto.setHistoryType(toHistoryTypeLabel(history.getHistoryType()));
        dto.setQuantity(history.getQuantity());
        dto.setCreatedAt(history.getCreatedAt());
        return dto;
    }

    private String toHistoryTypeLabel(String historyType) {
        return switch (historyType) {
            case "IN" -> "入庫";
            case "OUT" -> "出庫";
            case "ADJUST" -> "棚卸調整";
            default -> historyType;
        };
    }
}