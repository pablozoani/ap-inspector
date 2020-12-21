package com.pzoani.supermarket.controller;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.config.Urls;
import com.pzoani.supermarket.domain.Vendor;
import com.pzoani.supermarket.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class VendorControllerTest {

    @Mock
    VendorRepository vendorRepository;
    VendorController vendorController;
    final Inspector<Vendor> inspector =
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
        given(vendorRepository.findAll())
            .willReturn(Flux.just(vs));
        // then
        webTestClient.get()
            .uri(Urls.VENDORS_BASE_URL)
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
        given(vendorRepository.findById(anyString()))
            .willReturn(Mono.just(v));
        // then
        webTestClient.get()
            .uri(Urls.VENDORS_BASE_URL + "/any")
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
        when(vendorRepository.save(any(Vendor.class)))
            .thenReturn(Mono.just(v));
        // then
        webTestClient.post()
            .uri(Urls.VENDORS_BASE_URL)
            .body(Mono.just(v), Vendor.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(APPLICATION_JSON_VALUE)
            .expectBody(Vendor.class).isEqualTo(v);
        verify(vendorRepository, times(1)).save(any(Vendor.class));
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
        when(vendorRepository.existsById(id))
            .thenReturn(Mono.just(true));
        when(vendorRepository.save(v))
            .thenReturn(Mono.just(v));
        // then
        StepVerifier.create(
            webTestClient.put()
                .uri(Urls.VENDORS_BASE_URL + "/" + id)
                .body(Mono.just(v), Vendor.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .returnResult(Vendor.class)
                .getResponseBody()
        ).assertNext(vendor -> vendor.getId().equals(id));
    }

    @Test
    void deleteById() {
        String theId = "The ID";
        when(vendorRepository.existsById(theId))
            .thenReturn(Mono.just(true));
        when(vendorRepository.deleteById(any(String.class)))
            .thenReturn(Mono.empty());
        webTestClient
            .delete()
            .uri(Urls.VENDORS_BASE_URL + "/" + theId)
            .exchange()
            .expectStatus().isOk();
        verify(vendorRepository, times(1)).existsById(theId);
        verify(vendorRepository, times(1)).deleteById(theId);
        when(vendorRepository.existsById(theId))
            .thenReturn(Mono.just(false));
        webTestClient
            .delete()
            .uri(Urls.VENDORS_BASE_URL + "/" + theId)
            .exchange()
            .expectStatus().isNotFound();
    }
}