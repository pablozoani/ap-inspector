package com.pzoani.supermarket.controller;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.domain.Product;
import com.pzoani.supermarket.domain.Vendor;
import com.pzoani.supermarket.dto.ProductDTO;
import com.pzoani.supermarket.repository.CategoryRepository;
import com.pzoani.supermarket.repository.ProductRepository;
import com.pzoani.supermarket.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.pzoani.supermarket.config.Urls.PRODUCTS_BASE_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class ProductControllerTest {
    ProductController productController;
    @Mock
    ProductRepository productRepository;
    @Mock
    VendorRepository vendorRepository;
    @Mock
    CategoryRepository categoryRepository;
    final Inspector<Product> productInspector =
        new Inspector<>(new RuntimeException()) {
            @Override
            public Product inspect(Product product) {
                return product;
            }
        };
    Product product1;
    Product product2;
    ProductDTO productDTO1;
    WebTestClient webTestClient;
    Category c1;
    Vendor v1;
    Category c2;
    Vendor v2;

    @BeforeEach
    void setUp() {
        c1 = Category.builder().id("8").name("Best Seller").build();
        v1 = Vendor.builder().id("9").firstName("John").lastName("Doe").build();
        product1 = new Product(
            "7", "Coffee Machine", 22.99,
            c1.getId(), v1.getId()
        );
        c2 = Category.builder().id("10").name("Limited Edition").build();
        v2 = Vendor.builder().id("11").firstName("Foo").lastName("Bar").build();
        product2 = new Product(
            "13", "Notebook", 420.33,
            c2.getId(), v2.getId()
        );
        productDTO1 = new ProductDTO(product1.getId(), product1.getName(),
            product1.getPrice().doubleValue(), product1.getCategoryId(),
            product1.getVendorId()
        );
        MockitoAnnotations.initMocks(this);
        productController = new ProductController(
            productRepository, categoryRepository,
            vendorRepository, productInspector
        );
        webTestClient = WebTestClient.bindToController(productController)
            .build();
    }

    @Test
    void findAll() {
        given(productRepository.findAll())
            .willReturn(Flux.just(product1, product2));
        webTestClient
            .get()
            .uri(PRODUCTS_BASE_URL)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Product.class)
            .hasSize(2)
            .contains(product1, product2);
    }

    @Test
    void save() {
        given(productRepository.save(product1))
            .willReturn(Mono.just(product1));
        given(categoryRepository.existsById(product1.getCategoryId()))
            .willReturn(Mono.just(true));
        given(vendorRepository.existsById(product1.getVendorId()))
            .willReturn(Mono.just(true));

        webTestClient
            .post()
            .uri(PRODUCTS_BASE_URL)
            .body(Mono.just(productDTO1), ProductDTO.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody(Product.class)
            .isEqualTo(product1);

        verify(productRepository, times(1)).save(any(Product.class));
        verify(categoryRepository, times(1)).existsById(any(String.class));
        verify(vendorRepository, times(1)).existsById(any(String.class));
    }

    @Test
    void findById() {
        when(productRepository.findById(product1.getId()))
            .thenReturn(Mono.just(product1));
        webTestClient
            .get()
            .uri(PRODUCTS_BASE_URL + "/" + product1.getId())
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody(Product.class).isEqualTo(product1);
        verify(productRepository, times(1)).findById(product1.getId());
    }

    @Test
    void deleteById() {
        String theId = "The ID";
        when(productRepository.existsById(theId))
            .thenReturn(Mono.just(true));
        when(productRepository.deleteById(any(String.class)))
            .thenReturn(Mono.empty());
        webTestClient
            .delete()
            .uri(PRODUCTS_BASE_URL + "/" + theId)
            .exchange()
            .expectStatus().isOk();
        verify(productRepository, times(1)).existsById(theId);
        verify(productRepository, times(1)).deleteById(theId);
        when(productRepository.existsById(theId))
            .thenReturn(Mono.just(false));
        webTestClient
            .delete()
            .uri(PRODUCTS_BASE_URL + "/" + theId)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void update() {
        ProductDTO productDTO = new ProductDTO(product1.getId(),
            product1.getName(), product1.getPrice().doubleValue(),
            product1.getCategoryId(), product1.getVendorId()
        );
        when(categoryRepository.existsById(any(String.class)))
            .thenReturn(Mono.just(true));
        when(vendorRepository.existsById(any(String.class)))
            .thenReturn(Mono.just(true));
        when(productRepository.save(any(Product.class)))
            .thenReturn(Mono.just(product1));
        when(productRepository.existsById(productDTO.getId()))
            .thenReturn(Mono.just(true));
        webTestClient.put()
            .uri(PRODUCTS_BASE_URL + "/" + product1.getId())
            .body(Mono.just(productDTO), ProductDTO.class)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(APPLICATION_JSON_VALUE)
            .expectBody(Product.class)
            .isEqualTo(product1);
        verify(categoryRepository, times(1))
            .existsById(product1.getCategoryId());
        verify(vendorRepository, times(1))
            .existsById(product1.getVendorId());
        verify(productRepository, times(1))
            .save(any(Product.class));
    }
}