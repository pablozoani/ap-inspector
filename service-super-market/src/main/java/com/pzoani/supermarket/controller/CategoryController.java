package com.pzoani.supermarket.controller;

import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.paths.Endpoints;
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
@RequestMapping(Endpoints.CATEGORIES_BASE_URL)
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public Flux<Category> findAll() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Category> findById(@PathVariable("id") String id) {
        return categoryRepository.findById(id);
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = "application/json")
    public Mono<Category> save(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/{id}")
    public Mono<Category> update(@PathVariable("id") String id,
        @RequestBody Mono<Category> categoryMono
    ) {
        // TODO
        return categoryRepository.saveAll(categoryMono.map(c -> {
            c.setId(id);
            return c;
        })).next();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return categoryRepository.existsById(id)
            .flatMap(bool -> {
                if (bool) {
                    return categoryRepository.deleteById(id);
                } else {
                    throw new ResponseStatusException(
                        NOT_FOUND,
                        "Category " + id + " not found."
                    );
                }
            });
    }
}
