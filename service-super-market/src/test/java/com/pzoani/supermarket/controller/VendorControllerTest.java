package com.pzoani.supermarket.controller;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.domain.Vendor;
import com.pzoani.supermarket.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class VendorControllerTest {

    @Mock
    VendorRepository vendorRepository;
    VendorController vendorController;
    Inspector<Vendor> inspector =
        new Inspector<>(new RuntimeException()) {
            @Override
            public Vendor inspect(Vendor vendor) { return vendor; }
        };
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        vendorController = new VendorController(vendorRepository, inspector);
        webTestClient = WebTestClient
            .bindToController(vendorController)
            .build();
    }

    @Test
    void findAll() {
        // given
        Vendor[] vs = {
            Vendor.builder()
                .firstName("John")
                .lastName("Doe")
                .build(),
            Vendor.builder()
                .firstName("Foo")
                .lastName("Bar")
                .build()
        };
        // when
        BDDMockito
            .given(vendorRepository.findAll())
            .willReturn(Flux.just(vs));
        // then
        webTestClient.get()
            .uri("/api/v1/vendors")
            .exchange()
            .expectBodyList(Vendor.class)
            .hasSize(2)
            .contains(vs);
    }

    @Test
    void findById() {
        // given
        Vendor v = Vendor.builder()
            .firstName("John")
            .lastName("Doe")
            .build();
        // when
        BDDMockito.given(vendorRepository.findById(anyString()))
            .willReturn(Mono.just(v));
        // then
        webTestClient.get()
            .uri("/api/v1/vendors/any")
            .exchange()
            .expectBody(Vendor.class)
            .isEqualTo(v);
    }

    @Test
    void save() {
        // given
        Vendor v = Vendor.builder()
            .firstName("John")
            .lastName("Doe")
            .build();
        // when
        BDDMockito.when(vendorRepository.saveAll(any(Mono.class)))
            .thenReturn(Flux.just(v));
        // then
        webTestClient.post()
            .uri("/api/v1/vendors")
            .body(Mono.just(v), Vendor.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType("application/json");
    }

    @Test
    void update() {
        // given
        Vendor v = Vendor.builder()
            .firstName("John")
            .lastName("Doe")
            .build();
        String id = UUID.randomUUID().toString();
        // when
        BDDMockito.when(vendorRepository.saveAll(any(Mono.class)))
            .thenReturn(Flux.just(v));
        // then
        StepVerifier.create(
            webTestClient.put()
                .uri("/api/v1/vendors/" + id)
                .body(Mono.just(v), Vendor.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .returnResult(Vendor.class)
                .getResponseBody()
        ).assertNext(vendor -> vendor.getId().equals(id));
    }
}