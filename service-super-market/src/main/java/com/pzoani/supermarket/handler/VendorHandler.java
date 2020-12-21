package com.pzoani.supermarket.handler;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.domain.Vendor;
import com.pzoani.supermarket.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Profile("handlers")
@Component
public class VendorHandler {

    private final VendorRepository vendorRepository;
    private final Inspector<Vendor> inspector;
    private static final Class<Vendor> clazz = Vendor.class;

    @Autowired
    public VendorHandler(VendorRepository vendorRepository,
        Inspector<Vendor> inspector
    ) {
        this.vendorRepository = vendorRepository;
        this.inspector = inspector;
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(vendorRepository.findAll(), clazz);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest
            .bodyToMono(clazz)
            .doOnNext(inspector::inspect)
            .flatMap(vendorRepository::save)
            .flatMap(v -> ServerResponse
                .status(CREATED)
                .contentType(APPLICATION_JSON)
                .bodyValue(v)
            );
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return vendorRepository.findById(serverRequest.pathVariable("id"))
            .flatMap(v -> ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .bodyValue(v)
            ).switchIfEmpty(ServerResponse
                .notFound()
                .build()
            );
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return vendorRepository.findById(serverRequest.pathVariable("id"))
            .map(old -> serverRequest.bodyToMono(clazz)
                .doOnNext(inspector::inspect)
                .flatMap(niu -> {
                    niu.setId(old.getId());
                    return vendorRepository.save(niu);
                })
            ).flatMap(v -> ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(v, clazz)
            ).switchIfEmpty(ServerResponse
                .notFound()
                .build()
            );
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return vendorRepository.findById(serverRequest.pathVariable("id"))
            .flatMap(v -> vendorRepository
                .delete(v)
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
