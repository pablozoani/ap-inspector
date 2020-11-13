package com.pzoani.supermarket.handler;

import com.pzoani.supermarket.domain.Vendor;
import com.pzoani.supermarket.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Profile("handlers")
@Component
public class VendorHandler {

    private final VendorRepository vendorRepository;

    private static final Class<Vendor> clazz = Vendor.class;

    @Autowired
    public VendorHandler(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(vendorRepository.findAll(), clazz);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return Mono.from(serverRequest.bodyToFlux(clazz)
            .flatMap(vendorRepository::save)
        ).flatMap(v -> ServerResponse
            .created(URI.create(serverRequest.path() + "/" + v.getId()))
            .contentType(APPLICATION_JSON)
            .build()
        );
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(vendorRepository
                .findById(serverRequest.pathVariable("id")), clazz
            );
    }


    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return vendorRepository.findById(serverRequest.pathVariable("id"))
            .flatMap(old -> serverRequest.bodyToMono(clazz)
                .flatMap(niu -> {
                    niu.setId(old.getId());
                    return vendorRepository.save(niu);
                })
            ).flatMap(v -> ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .build()
            );
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return vendorRepository.deleteById(serverRequest.pathVariable("id"))
            .flatMap(boid -> ServerResponse
                .ok()
                .build()
            );
    }
}
