package com.example.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
}