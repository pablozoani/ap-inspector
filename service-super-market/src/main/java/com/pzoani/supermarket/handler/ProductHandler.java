package com.pzoani.supermarket.handler;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.domain.Product;
import com.pzoani.supermarket.repository.CategoryRepository;
import com.pzoani.supermarket.repository.ProductRepository;
import com.pzoani.supermarket.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Profile("handlers")
@Component
public class ProductHandler {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;
    private final Inspector<Product> inspector;
    private final Class<Product> clazz = Product.class;

    @Autowired
    public ProductHandler(ProductRepository productRepository,
        CategoryRepository categoryRepository,
        VendorRepository vendorRepository, Inspector<Product> inspector
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
        this.inspector = inspector;
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(productRepository.findAll(), clazz);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(clazz)
            .doOnNext(inspector::inspect)
            .flatMap(p -> categoryRepository
                .existsById(p.getCategoryId())
                .zipWith(vendorRepository.existsById(p.getVendorId()))
                .flatMap(tuple -> {
                    if (!tuple.getT1()) {
                        return Mono.error(new ResponseStatusException(
                            NOT_FOUND, "Category " + p.getCategoryId() +
                            " not found."
                        ));
                    } else if (!tuple.getT2()) {
                        return Mono.error(new ResponseStatusException(
                            NOT_FOUND, "Vendor " + p.getVendorId() +
                            " not found."
                        ));
                    } else {
                        return productRepository.save(p);
                    }
                })
            ).flatMap(p -> ServerResponse
                .status(CREATED)
                .contentType(APPLICATION_JSON)
                .bodyValue(p)
            );
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return productRepository.findById(serverRequest.pathVariable("id"))
            .flatMap(p -> ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .bodyValue(p)
            ).switchIfEmpty(ServerResponse
                .notFound()
                .build()
            );
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return productRepository.findById(serverRequest.pathVariable("id"))
            .flatMap(old -> serverRequest.bodyToMono(Product.class)
                .doOnNext(inspector::inspect)
                .flatMap(niu -> categoryRepository
                    .existsById(niu.getCategoryId())
                    .zipWith(vendorRepository.existsById(niu.getVendorId()))
                    .flatMap(tuple -> {
                        if (!tuple.getT1()) {
                            return Mono.error(new ResponseStatusException(
                                NOT_FOUND, "Category " + niu.getCategoryId() +
                                " not found."
                            ));
                        } else if (!tuple.getT2()) {
                            return Mono.error(new ResponseStatusException(
                                NOT_FOUND, "Vendor " + niu.getVendorId() +
                                " not found."
                            ));
                        } else {
                            niu.setId(old.getId());
                            return productRepository.save(niu);
                        }
                    })
                )
            ).flatMap(p -> ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .bodyValue(p)
            ).switchIfEmpty(ServerResponse
                .notFound()
                .build()
            );
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return productRepository.findById(serverRequest.pathVariable("id"))
            .flatMap(p -> productRepository
                .delete(p)
                .then(ServerResponse
                    .ok()
                    .build()
                )
            ).switchIfEmpty(ServerResponse
                .notFound()
                .build()
            );
    }
}
