package com.pzoani.supermarket.controller;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.config.Urls;
import com.pzoani.supermarket.domain.Product;
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
@RequestMapping(Urls.PRODUCTS_BASE_URL)
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;
    private final Inspector<Product> inspector;

    @Autowired
    public ProductController(ProductRepository productRepository,
        CategoryRepository categoryRepository,
        VendorRepository vendorRepository, Inspector<Product> inspector
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
        this.inspector = inspector;
    }

    @GetMapping
    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Product> findById(@PathVariable("id") String id) {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(
                NOT_FOUND,
                "Product " + id + " not found."
            )));
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = "application/json")
    public Mono<Product> save(@RequestBody Product product) {
        inspector.inspect(product);
        return categoryRepository
            .existsById(product.getCategoryId())
            .zipWith(vendorRepository.existsById(product.getVendorId()))
            .flatMap(tuple -> {
                if (!tuple.getT1()) {
                    throw new ResponseStatusException(NOT_FOUND,
                        "Product's category not found."
                    );
                } else if (!tuple.getT2()) {
                    throw new ResponseStatusException(NOT_FOUND,
                        "Product's vendor not found."
                    );
                } else {
                    return productRepository.save(inspector.inspect(
                        Product.builder()
                            .name(product.getName())
                            .price(product.getPrice())
                            .categoryId(product.getCategoryId())
                            .vendorId(product.getVendorId())
                            .build()
                    ));
                }
            });
    }


    @PutMapping(path = "/{id}", consumes = "application/json")
    public Mono<Product> update(@PathVariable("id") String id,
        @RequestBody Product product
    ) {
        inspector.inspect(product);
        return categoryRepository.existsById(product.getCategoryId())
            .zipWith(vendorRepository.existsById(product.getVendorId()))
            .flatMap(tuple -> {
                if (!tuple.getT1()) {
                    throw new ResponseStatusException(NOT_FOUND,
                        "Product's category not found."
                    );
                } else if (!tuple.getT2()) {
                    throw new ResponseStatusException(NOT_FOUND,
                        "Product's vendor not found."
                    );
                } else {
                    return productRepository.existsById(id)
                        .flatMap(bool -> {
                            if (bool) {
                                return productRepository.save(Product
                                    .builder()
                                    .id(id)
                                    .price(product.getPrice())
                                    .name(product.getName())
                                    .categoryId(product.getCategoryId())
                                    .vendorId(product.getVendorId())
                                    .build()
                                );
                            } else {
                                throw new ResponseStatusException(
                                    NOT_FOUND,
                                    "Product " + id + " not found."
                                );
                            }
                        });
                }
            });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return productRepository.existsById(id)
            .flatMap(bool -> {
                if (bool) {
                    return productRepository.deleteById(id);
                } else {
                    throw new ResponseStatusException(NOT_FOUND,
                        "Product " + id + " not found."
                    );
                }
            });
    }
}
