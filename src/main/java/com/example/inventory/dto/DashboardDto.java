package com.example.inventory.dto;

import java.util.List;

public class DashboardDto {

    private long productCount;
    private long warehouseCount;
    private long stockCount;
    private long stockHistoryCount;

    private List<LowStockDto> lowStocks;
    private List<RecentStockHistoryDto> recentHistories;

    public long getProductCount() {
        return productCount;
    }

    public void setProductCount(long productCount) {
        this.productCount = productCount;
    }

    public long getWarehouseCount() {
        return warehouseCount;
    }

    public void setWarehouseCount(long warehouseCount) {
        this.warehouseCount = warehouseCount;
    }

    public long getStockCount() {
        return stockCount;
    }

    public void setStockCount(long stockCount) {
        this.stockCount = stockCount;
    }

    public long getStockHistoryCount() {
        return stockHistoryCount;
    }

    public void setStockHistoryCount(long stockHistoryCount) {
        this.stockHistoryCount = stockHistoryCount;
    }

    public List<LowStockDto> getLowStocks() {
        return lowStocks;
    }

    public void setLowStocks(List<LowStockDto> lowStocks) {
        this.lowStocks = lowStocks;
    }

    public List<RecentStockHistoryDto> getRecentHistories() {
        return recentHistories;
    }

    public void setRecentHistories(List<RecentStockHistoryDto> recentHistories) {
        this.recentHistories = recentHistories;
    }
}