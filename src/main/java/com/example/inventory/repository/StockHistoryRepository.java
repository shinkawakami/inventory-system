package com.example.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.StockHistory;

@Repository
public interface StockHistoryRepository extends JpaRepository<StockHistory, Integer> {
}