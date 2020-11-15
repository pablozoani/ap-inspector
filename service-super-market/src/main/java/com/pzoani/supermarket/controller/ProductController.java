package com.pzoani.supermarket.controller;

import com.pzoani.supermarket.DTO.ProductDTO;
import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.domain.Product;
import com.pzoani.supermarket.domain.Vendor;
import com.pzoani.supermarket.paths.Endpoints;
import com.pzoani.supermarket.repository.CategoryRepository;
import com.pzoani.supermarket.repository.ProductRepository;
import com.pzoani.supermarket.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Profile("controllers")
@RestController
@RequestMapping(Endpoints.PRODUCTS_BASE_URL)
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    @Autowired
    public ProductController(ProductRepository productRepository,
        CategoryRepository categoryRepository, VendorRepository vendorRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = "application/json")
    public Mono<Product> save(@RequestBody ProductDTO productDTO) {
        Mono<Category> categoryMono = categoryRepository
            .findById(productDTO.getCategoryId());
        Mono<Vendor> vendorMono = vendorRepository
            .findById(productDTO.getVendorId());
        return vendorMono.zipWith(categoryMono)
            .flatMap(tuple -> {
                Vendor vendor = tuple.getT1();
                Category category = tuple.getT2();
                return productRepository.save(Product.builder()
                    .name(productDTO.getName())
                    .price(productDTO.getPrice())
                    .category(category)
                    .vendor(vendor)
                    .build()
                );
            });
    }

    @PutMapping("/{id}")
    public Mono<Product> update(@PathVariable("id") String id,
        @RequestBody ProductDTO productDTO
    ) {
        Mono<Category> categoryMono = categoryRepository
            .findById(productDTO.getCategoryId());
        Mono<Vendor> vendorMono = vendorRepository
            .findById(productDTO.getVendorId());
        return vendorMono.zipWith(categoryMono)
            .flatMap(tuple -> productRepository.save(Product
                .builder()
                .id(id)
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .vendor(tuple.getT1())
                .category(tuple.getT2())
                .build()
            ));
    }

    @GetMapping("/{id}")
    public Mono<Product> findById(@PathVariable("id") String id) {
        return productRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return productRepository.existsById(id)
            .flatMap(bool -> {
                if (bool) {
                    return productRepository.deleteById(id);
                } else {
                    throw new ResponseStatusException(
                        NOT_FOUND,
                        "Product " + id + " not found."
                    );
                }
            });
    }
}
