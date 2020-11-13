package com.pzoani.supermarket.handler;

import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.repository.CategoryRepository;
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
public class CategoryHandler {

    private final CategoryRepository categoryRepository;

    private static final Class<Category> clazz = Category.class;

    @Autowired
    public CategoryHandler(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(categoryRepository.findAll(), clazz);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(categoryRepository
                .findById(serverRequest.pathVariable("id")), clazz
            );
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return Mono.from(serverRequest.bodyToFlux(clazz)
            .flatMap(categoryRepository::save)
        ).flatMap(c -> ServerResponse
            .created(URI.create(serverRequest.path() + "/" + c.getId()))
            .contentType(APPLICATION_JSON)
            .build()
        );
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return categoryRepository.findById(serverRequest.pathVariable("id"))
            .flatMap(old -> serverRequest.bodyToMono(clazz)
                .flatMap(niu -> {
                    niu.setId(old.getId());
                    return categoryRepository.save(niu);
                })
            ).flatMap(c -> ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .build()
            );
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return categoryRepository.deleteById(serverRequest.pathVariable("id"))
            .flatMap(boid -> ServerResponse
                .ok()
                .build()
            );
    }
}
