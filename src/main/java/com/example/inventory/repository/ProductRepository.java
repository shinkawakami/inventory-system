package com.example.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}