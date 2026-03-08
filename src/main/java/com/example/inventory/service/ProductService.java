package com.example.inventory.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.inventory.entity.Product;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.form.ProductForm;
import com.example.inventory.form.ProductSearchForm;
import com.example.inventory.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return repository.findAllByOrderByProductIdAsc();
    }

    @Transactional
    public void save(ProductForm form) {
        Product product = new Product();
        product.setProductName(form.getProductName());
        product.setPrice(form.getPrice());
        repository.save(product);
    }

    @Transactional(readOnly = true)
    public Product findById(Integer productId) {
        return repository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("商品が見つかりません。"));
    }

    @Transactional
    public void update(ProductForm form) {
        Product product = repository.findById(form.getProductId()).orElseThrow();
        product.setProductName(form.getProductName());
        product.setPrice(form.getPrice());
        repository.save(product);
    }

    @Transactional
    public void delete(Integer productId) {
        repository.deleteById(productId);
    }

    @Transactional(readOnly = true)
    public Page<Product> search(ProductSearchForm form, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (form == null || !StringUtils.hasText(form.getProductName())) {
            return repository.findAllByOrderByProductIdAsc(pageable);
        }

        return repository.findByProductNameContainingOrderByProductIdAsc(
                form.getProductName().trim(),
                pageable
        );
    }
}