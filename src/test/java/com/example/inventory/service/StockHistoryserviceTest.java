package com.example.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.inventory.entity.Product;
import com.example.inventory.entity.Stock;
import com.example.inventory.entity.StockHistory;
import com.example.inventory.entity.Warehouse;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.form.StockHistoryForm;
import com.example.inventory.repository.StockHistoryRepository;
import com.example.inventory.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockHistoryServiceTest {

    @Mock
    private StockHistoryRepository stockHistoryRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockHistoryService stockHistoryService;

    private Stock stock;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("ノートPC");

        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(1);
        warehouse.setWarehouseName("東京倉庫");

        stock = new Stock();
        stock.setStockId(1);
        stock.setProduct(product);
        stock.setWarehouse(warehouse);
        stock.setQuantity(10);
    }

    @Test
    void save_IN_在庫が加算され履歴が保存される() {
        StockHistoryForm form = new StockHistoryForm();
        form.setStockId(1);
        form.setHistoryType("IN");
        form.setQuantity(5);
        form.setNote("入庫テスト");

        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        stockHistoryService.save(form);

        assertEquals(15, stock.getQuantity());
        verify(stockHistoryRepository, times(1)).save(any(StockHistory.class));
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void save_OUT_在庫が減算され履歴が保存される() {
        StockHistoryForm form = new StockHistoryForm();
        form.setStockId(1);
        form.setHistoryType("OUT");
        form.setQuantity(4);
        form.setNote("出庫テスト");

        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        stockHistoryService.save(form);

        assertEquals(6, stock.getQuantity());
        verify(stockHistoryRepository, times(1)).save(any(StockHistory.class));
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void save_OUT_在庫不足ならBusinessException() {
        StockHistoryForm form = new StockHistoryForm();
        form.setStockId(1);
        form.setHistoryType("OUT");
        form.setQuantity(11);
        form.setNote("在庫不足テスト");

        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> stockHistoryService.save(form)
        );

        assertEquals("在庫数が不足しています。", ex.getMessage());
        verify(stockHistoryRepository, times(0)).save(any(StockHistory.class));
        verify(stockRepository, times(0)).save(any(Stock.class));
    }

    @Test
    void save_ADJUST_在庫数が指定値に更新される() {
        StockHistoryForm form = new StockHistoryForm();
        form.setStockId(1);
        form.setHistoryType("ADJUST");
        form.setQuantity(3);
        form.setNote("棚卸調整テスト");

        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        stockHistoryService.save(form);

        assertEquals(3, stock.getQuantity());
        verify(stockHistoryRepository, times(1)).save(any(StockHistory.class));
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void save_不正な履歴種別ならBusinessException() {
        StockHistoryForm form = new StockHistoryForm();
        form.setStockId(1);
        form.setHistoryType("XXX");
        form.setQuantity(1);

        when(stockRepository.findById(1)).thenReturn(Optional.of(stock));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> stockHistoryService.save(form)
        );

        assertEquals("不正な履歴種別です。", ex.getMessage());
    }
}