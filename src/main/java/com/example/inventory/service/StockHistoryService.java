package com.example.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.entity.Stock;
import com.example.inventory.entity.StockHistory;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.form.StockHistoryForm;
import com.example.inventory.repository.StockHistoryRepository;
import com.example.inventory.repository.StockRepository;

@Service
public class StockHistoryService {

    private final StockHistoryRepository stockHistoryRepository;
    private final StockRepository stockRepository;

    public StockHistoryService(StockHistoryRepository stockHistoryRepository,
                               StockRepository stockRepository) {
        this.stockHistoryRepository = stockHistoryRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional(readOnly = true)
    public List<StockHistory> findAll() {
        return stockHistoryRepository.findAllByOrderByCreatedAtDescHistoryIdDesc();
    }

    @Transactional
    public void save(StockHistoryForm form) {
        Stock stock = stockRepository.findById(form.getStockId())
            .orElseThrow(() -> new ResourceNotFoundException("在庫が見つかりません。"));

        int currentQty = stock.getQuantity();
        int moveQty = form.getQuantity();

        switch (form.getHistoryType()) {
            case "IN":
                stock.setQuantity(currentQty + moveQty);
                break;
            case "OUT":
                if (currentQty < moveQty) {
                    throw new BusinessException("在庫数が不足しています。");
                }
                stock.setQuantity(currentQty - moveQty);
                break;
            case "ADJUST":
                stock.setQuantity(moveQty);
                break;
            default:
                throw new BusinessException("不正な履歴種別です。");
        }

        StockHistory history = new StockHistory();
        history.setStock(stock);
        history.setHistoryType(form.getHistoryType());
        history.setQuantity(form.getQuantity());
        history.setNote(form.getNote());

        stockHistoryRepository.save(history);
        stockRepository.save(stock);
    }
}