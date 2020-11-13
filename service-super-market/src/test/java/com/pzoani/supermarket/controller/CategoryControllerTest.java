package com.pzoani.supermarket.controller;

import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class CategoryControllerTest {

    WebTestClient webTestClient;

    @Mock
    private CategoryRepository categoryRepository;

    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void findAll() {
        Category[] cs = {
            Category.builder().description("IceCream").build(),
            Category.builder().description("Food").build()
        };
        // when
        BDDMockito.given(categoryRepository.findAll())
            .willReturn(Flux.just(cs));
        // then
        webTestClient
            .get().uri("/api/v1/categories")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBodyList(Category.class)
            .hasSize(2)
            .contains(cs);
    }

    @Test
    void findById() {
        // given
        Category c = Category.builder()
            .description("IceCream")
            .build();
        // when
        BDDMockito.given(categoryRepository.findById(anyString()))
            .willReturn(Mono.just(c));
        // then
        webTestClient
            .get().uri("/api/v1/categories/anid")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody(Category.class)
            .isEqualTo(c);
    }

    @Test
    void save() {
        // given
        Mono<Category> m = Mono.just(Category.builder().description("Food").build());
        // when
        BDDMockito.when(categoryRepository.saveAll(any(Publisher.class)))
            .thenReturn(Flux.just(new Category()));
        // then
        webTestClient.post()
            .uri("/api/v1/categories")
            .body(m, Category.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType("application/json");
    }

    @Test
    void update() {
        // given
        Category c = Category.builder().description("Food").build();
        String id = UUID.randomUUID().toString();
        // when
        BDDMockito.when(categoryRepository.saveAll(any(Mono.class)))
            .thenReturn(Flux.just(c));
        // then
        StepVerifier.create(webTestClient.put().uri("/api/v1/categories/" + id)
            .body(Mono.just(c), Category.class)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType("application/json")
            .returnResult(Category.class)
            .getResponseBody()
        ).assertNext(category -> category.getId().equals(id));
    }
}