package com.pzoani.supermarket.controller;

import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.paths.Endpoints;
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
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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
            Category.builder().name("IceCream").build(),
            Category.builder().name("Food").build()
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
            .name("IceCream")
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
        Category category = Category.builder().name("Food").build();
        // when
        BDDMockito.given(categoryRepository.save(any(Category.class)))
            .willReturn(Mono.just(category));
        // then
        webTestClient.post()
            .uri(Endpoints.CATEGORIES_BASE_URL)
            .body(Mono.just(category), Category.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType("application/json")
            .expectBody(Category.class)
            .isEqualTo(category);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void update() {
        // given
        Category c = Category.builder().name("Food").build();
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

    @Test
    void deleteById() {
        String theId = "The ID";
        when(categoryRepository.existsById(theId))
            .thenReturn(Mono.just(true));
        when(categoryRepository.deleteById(any(String.class)))
            .thenReturn(Mono.empty());
        webTestClient
            .delete()
            .uri(Endpoints.CATEGORIES_BASE_URL + "/" + theId)
            .exchange()
            .expectStatus().isOk();
        verify(categoryRepository, times(1)).existsById(theId);
        verify(categoryRepository, times(1)).deleteById(theId);
        when(categoryRepository.existsById(theId))
            .thenReturn(Mono.just(false));
        webTestClient
            .delete()
            .uri(Endpoints.CATEGORIES_BASE_URL + "/" + theId)
            .exchange()
            .expectStatus().isNotFound();
    }
}