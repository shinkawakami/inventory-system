package com.example.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import com.example.inventory.dto.DashboardDto;
import com.example.inventory.entity.Product;
import com.example.inventory.entity.Stock;
import com.example.inventory.entity.StockHistory;
import com.example.inventory.entity.Warehouse;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.StockHistoryRepository;
import com.example.inventory.repository.StockRepository;
import com.example.inventory.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockHistoryRepository stockHistoryRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void getDashboard_件数と低在庫一覧と最近履歴が正しく設定される() {
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("ノートPC");

        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(1);
        warehouse.setWarehouseName("東京倉庫");

        Stock stock = new Stock();
        stock.setStockId(10);
        stock.setProduct(product);
        stock.setWarehouse(warehouse);
        stock.setQuantity(3);

        StockHistory history = new StockHistory();
        history.setHistoryId(100);
        history.setStock(stock);
        history.setHistoryType("IN");
        history.setQuantity(5);

        when(productRepository.count()).thenReturn(5L);
        when(warehouseRepository.count()).thenReturn(2L);
        when(stockRepository.count()).thenReturn(8L);
        when(stockHistoryRepository.count()).thenReturn(20L);

        when(stockRepository.findTop5ByQuantityLessThanEqualOrderByQuantityAscStockIdAsc(5))
                .thenReturn(List.of(stock));

        when(stockHistoryRepository.findTop5ByOrderByCreatedAtDescHistoryIdDesc())
                .thenReturn(List.of(history));

        DashboardDto result = dashboardService.getDashboard();

        assertNotNull(result);
        assertEquals(5L, result.getProductCount());
        assertEquals(2L, result.getWarehouseCount());
        assertEquals(8L, result.getStockCount());
        assertEquals(20L, result.getStockHistoryCount());

        assertEquals(1, result.getLowStocks().size());
        assertEquals(10, result.getLowStocks().get(0).getStockId());
        assertEquals("ノートPC", result.getLowStocks().get(0).getProductName());
        assertEquals("東京倉庫", result.getLowStocks().get(0).getWarehouseName());
        assertEquals(3, result.getLowStocks().get(0).getQuantity());

        assertEquals(1, result.getRecentHistories().size());
        assertEquals(100, result.getRecentHistories().get(0).getHistoryId());
        assertEquals("ノートPC", result.getRecentHistories().get(0).getProductName());
        assertEquals("東京倉庫", result.getRecentHistories().get(0).getWarehouseName());
        assertEquals("入庫", result.getRecentHistories().get(0).getHistoryType());
        assertEquals(5, result.getRecentHistories().get(0).getQuantity());
    }

    @Test
    void getDashboard_データが空でも空リストで返す() {
        when(productRepository.count()).thenReturn(0L);
        when(warehouseRepository.count()).thenReturn(0L);
        when(stockRepository.count()).thenReturn(0L);
        when(stockHistoryRepository.count()).thenReturn(0L);

        when(stockRepository.findTop5ByQuantityLessThanEqualOrderByQuantityAscStockIdAsc(5))
                .thenReturn(List.of());

        when(stockHistoryRepository.findTop5ByOrderByCreatedAtDescHistoryIdDesc())
                .thenReturn(List.of());

        DashboardDto result = dashboardService.getDashboard();

        assertNotNull(result);
        assertEquals(0L, result.getProductCount());
        assertEquals(0L, result.getWarehouseCount());
        assertEquals(0L, result.getStockCount());
        assertEquals(0L, result.getStockHistoryCount());
        assertEquals(0, result.getLowStocks().size());
        assertEquals(0, result.getRecentHistories().size());
    }
}