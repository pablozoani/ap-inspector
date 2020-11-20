package com.pzoani.supermarket.handler;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.repository.CategoryRepository;
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
public class CategoryHandler {

    private final CategoryRepository categoryRepository;
    private final Inspector<Category> inspector;
    private static final Class<Category> clazz = Category.class;

    @Autowired
    public CategoryHandler(CategoryRepository categoryRepository,
        Inspector<Category> inspector
    ) {
        this.categoryRepository = categoryRepository;
        this.inspector = inspector;
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(categoryRepository.findAll(), clazz);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest
            .bodyToMono(clazz)
            .doOnNext(inspector::inspect)
            .flatMap(categoryRepository::save)
            .flatMap(c -> ServerResponse
                .status(CREATED)
                .contentType(APPLICATION_JSON)
                .bodyValue(c)
            );
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return categoryRepository.findById(serverRequest.pathVariable("id"))
            .flatMap(category -> ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .bodyValue(category)
            ).switchIfEmpty(ServerResponse
                .notFound()
                .build()
            );
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return categoryRepository.findById(serverRequest.pathVariable("id"))
            .map(old -> serverRequest.bodyToMono(clazz)
                .doOnNext(inspector::inspect)
                .flatMap(niu -> {
                    niu.setId(old.getId());
                    return categoryRepository.save(niu);
                })
            ).flatMap(c -> ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(c, clazz)
            ).switchIfEmpty(ServerResponse
                .notFound()
                .build()
            );
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return categoryRepository.findById(serverRequest.pathVariable("id"))
            .flatMap(c -> categoryRepository.delete(c)
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
