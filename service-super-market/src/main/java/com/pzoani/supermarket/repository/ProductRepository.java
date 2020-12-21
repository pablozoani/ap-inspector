package com.pzoani.supermarket.repository;

import com.pzoani.supermarket.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository
    extends ReactiveMongoRepository<Product, String> {

    Flux<Product> findBy(Pageable pageable);

    Flux<Product> findByNameRegex(String regex, Pageable pageable);

    Flux<Product> findByCategoryId(String categoryId, Pageable pageable);

    Flux<Product> findByVendorId(String vendorId, Pageable pageable);

    Flux<Product> findByNameRegexAndCategoryId(String regex,
        String categoryId, Pageable pageable
    );

    Flux<Product> findByNameRegexAndVendorId(String regex,
        String vendorId, Pageable pageable
    );

    Mono<Long> countByNameRegex(String regex);

    Mono<Long> countByCategoryId(String customerId);

    Mono<Long> countByVendorId(String vendorId);

    Mono<Long> countByNameRegexAndCategoryId(String regex, String categoryId);

    Mono<Long> countByNameRegexAndVendorId(String regex, String vendorId);
}
