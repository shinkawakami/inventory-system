package com.example.inventory.form;

import jakarta.validation.constraints.NotNull;

public class StockForm {

    private Integer stockId;

    @NotNull(message = "商品は必須です")
    private Integer productId;

    @NotNull(message = "倉庫は必須です")
    private Integer warehouseId;

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
}