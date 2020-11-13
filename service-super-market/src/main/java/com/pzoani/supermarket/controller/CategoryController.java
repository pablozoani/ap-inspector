package com.pzoani.supermarket.controller;

import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.paths.Endpoints;
import com.pzoani.supermarket.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json")
    public Mono<Category> save(@RequestBody Mono<Category> categoryMono) {
        return categoryRepository.saveAll(categoryMono).next();
    }

    @PutMapping("/{id}")
    public Mono<Category> update(@PathVariable("id") String id,
        @RequestBody Mono<Category> categoryMono) {
        return categoryRepository.saveAll(categoryMono.map(c -> {
            c.setId(id);
            return c;
        })).next();
    }
}
