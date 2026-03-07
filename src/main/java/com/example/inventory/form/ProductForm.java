package com.example.inventory.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductForm {

    private Integer productId;

    @NotBlank(message = "商品名は必須です")
    @Size(max = 100, message = "商品名は100文字以内で入力してください")
    private String productName;

    @NotNull(message = "価格は必須です")
    @Min(value = 0, message = "価格は0以上で入力してください")
    private Integer price;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}