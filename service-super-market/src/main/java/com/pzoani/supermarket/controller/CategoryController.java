package com.pzoani.supermarket.controller;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.config.Urls;
import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.repository.CategoryRepository;
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
@RequestMapping(Urls.CATEGORIES_BASE_URL)
public class CategoryController {

    private final CategoryRepository categoryRepository;

    private final Inspector<Category> inspector;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository,
        Inspector<Category> inspector
    ) {
        this.categoryRepository = categoryRepository;
        this.inspector = inspector;
    }

    @GetMapping
    public Flux<Category> findAll() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Category> findById(@PathVariable("id") String id) {
        return categoryRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(
                NOT_FOUND, "Category " + id + " not found."
            )));
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = "application/json")
    public Mono<Category> save(@RequestBody Category category) {
        return categoryRepository.save(inspector.inspect(category));
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Mono<Category> update(@PathVariable("id") String id,
        @RequestBody Category category
    ) {
        return categoryRepository.existsById(id)
            .flatMap(bool -> {
                if (bool) {
                    inspector.inspect(category);
                    category.setId(id);
                    return categoryRepository.save(category);
                } else {
                    throw new ResponseStatusException(NOT_FOUND,
                        "Category " + id + " not found."
                    );
                }
            });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return categoryRepository.existsById(id)
            .flatMap(bool -> {
                if (bool) {
                    return categoryRepository.deleteById(id);
                } else {
                    throw new ResponseStatusException(NOT_FOUND,
                        "Category " + id + " not found."
                    );
                }
            });
    }
}
