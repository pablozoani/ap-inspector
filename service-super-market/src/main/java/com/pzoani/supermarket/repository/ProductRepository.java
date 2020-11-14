package com.pzoani.supermarket.repository;

import com.pzoani.supermarket.domain.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository
    extends ReactiveMongoRepository<Product, String> {
}
