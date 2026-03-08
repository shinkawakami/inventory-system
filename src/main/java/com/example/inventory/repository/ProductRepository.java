package com.example.inventory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByOrderByProductIdAsc();

    List<Product> findByProductNameContainingOrderByProductIdAsc(String productName);

    Page<Product> findAllByOrderByProductIdAsc(Pageable pageable);

    Page<Product> findByProductNameContainingOrderByProductIdAsc(String productName, Pageable pageable);
}