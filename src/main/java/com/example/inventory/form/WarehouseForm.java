package com.example.inventory.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class WarehouseForm {

    private Integer warehouseId;

    @NotBlank(message = "倉庫名は必須です")
    @Size(max = 100, message = "倉庫名は100文字以内で入力してください")
    private String warehouseName;

    @Size(max = 255, message = "所在地は255文字以内で入力してください")
    private String location;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}