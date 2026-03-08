package com.example.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import com.example.inventory.entity.Product;
import com.example.inventory.entity.Stock;
import com.example.inventory.entity.Warehouse;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.form.StockForm;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.StockRepository;
import com.example.inventory.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private StockService stockService;

    @Test
    void save_重複データならBusinessException() {
        StockForm form = new StockForm();
        form.setProductId(1);
        form.setWarehouseId(1);

        when(stockRepository.existsByProductProductIdAndWarehouseWarehouseId(1, 1))
                .thenReturn(true);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> stockService.save(form)
        );

        assertEquals("同じ商品・同じ倉庫の在庫が既に登録されています。", ex.getMessage());
        verify(stockRepository, times(0)).save(any(Stock.class));
    }

    @Test
    void save_重複でなければ数量0で保存される() {
        StockForm form = new StockForm();
        form.setProductId(1);
        form.setWarehouseId(1);

        Product product = new Product();
        product.setProductId(1);

        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(1);

        when(stockRepository.existsByProductProductIdAndWarehouseWarehouseId(1, 1))
                .thenReturn(false);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(warehouseRepository.findById(1)).thenReturn(Optional.of(warehouse));

        stockService.save(form);

        verify(stockRepository, times(1)).save(any(Stock.class));
    }
}