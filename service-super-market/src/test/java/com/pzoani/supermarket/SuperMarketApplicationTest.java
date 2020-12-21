package com.pzoani.supermarket;

import com.pzoani.supermarket.config.Urls;
import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.domain.Product;
import com.pzoani.supermarket.domain.Vendor;
import com.pzoani.supermarket.repository.CategoryRepository;
import com.pzoani.supermarket.repository.ProductRepository;
import com.pzoani.supermarket.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles({"handlers"})
@AutoConfigureWebTestClient
@SpringBootTest
public class SuperMarketApplicationTest {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    VendorRepository vendorRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll().block();
        productRepository.deleteAll().block();
        vendorRepository.deleteAll().block();
    }

    @Test
    void categoryCRUD() {

        System.out.println("- CREATE");

        Category category = Category.builder()
            .name("Best Seller")
            .build();

        Category savedCategory = webTestClient.post()
            .uri(Urls.CATEGORIES_BASE_URL)
            .body(Mono.just(category), category.getClass())
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Category.class).isEqualTo(category)
            .consumeWith(this::printResult)
            .returnResult().getResponseBody();

        assertEquals(category, savedCategory);

        Category badCategory = Category
            .builder()
            .name("a")
            .build();

        webTestClient.post()
            .uri(Urls.CATEGORIES_BASE_URL)
            .body(Mono.just(badCategory), Category.class)
            .exchange()
            .expectStatus().isBadRequest();

        assertEquals(1, categoryRepository.count().block());

        System.out.println("- READ");

        Category foundCategory = webTestClient.get()
            .uri(Urls.CATEGORIES_BASE_URL + "/" + savedCategory.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody(Category.class).isEqualTo(savedCategory)
            .consumeWith(this::printResult)
            .returnResult().getResponseBody();

        assertEquals(savedCategory, foundCategory);

        webTestClient.get()
            .uri(Urls.CATEGORIES_BASE_URL + "/foo")
            .exchange()
            .expectStatus().isNotFound();

        System.out.println("- UPDATE");

        Category newCategory = Category.builder()
            .name("Limited Edition")
            .build();

        Category updatedCategory = webTestClient.put()
            .uri(Urls.CATEGORIES_BASE_URL + "/" + savedCategory.getId())
            .body(Mono.just(newCategory), category.getClass())
            .exchange()
            .expectStatus().isOk()
            .expectBody(Category.class).isEqualTo(newCategory)
            .consumeWith(this::printResult)
            .returnResult().getResponseBody();

        assertEquals(savedCategory.getId(), updatedCategory.getId());

        webTestClient.put()
            .uri(Urls.CATEGORIES_BASE_URL + "/foo")
            .body(Mono.just(newCategory), category.getClass())
            .exchange()
            .expectStatus().isNotFound();

        System.out.println("- DELETE");

        webTestClient.delete()
            .uri(Urls.CATEGORIES_BASE_URL + "/" + updatedCategory.getId())
            .exchange()
            .expectStatus().isOk();

        assertEquals(0, categoryRepository.count().block());

        webTestClient.delete()
            .uri(Urls.CATEGORIES_BASE_URL + "/foo")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void vendorCRUD() {

        System.out.println("- CREATE");

        Vendor vendor = Vendor.builder()
            .firstName("John")
            .lastName("Doe")
            .build();

        Vendor savedVendor = webTestClient.post()
            .uri(Urls.VENDORS_BASE_URL)
            .body(Mono.just(vendor), vendor.getClass())
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Vendor.class).isEqualTo(vendor)
            .consumeWith(this::printResult)
            .returnResult().getResponseBody();

        assertEquals(vendor, savedVendor);

        Vendor badVendor = Vendor
            .builder()
            .firstName("a")
            .lastName("b")
            .build();

        webTestClient.post()
            .uri(Urls.VENDORS_BASE_URL)
            .body(Mono.just(badVendor), Vendor.class)
            .exchange()
            .expectStatus().isBadRequest();

        assertEquals(1, vendorRepository.count().block());

        System.out.println("- READ");

        Vendor foundVendor = webTestClient.get()
            .uri(Urls.VENDORS_BASE_URL + "/" + savedVendor.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody(Vendor.class).isEqualTo(savedVendor)
            .consumeWith(this::printResult)
            .returnResult().getResponseBody();

        assertEquals(savedVendor, foundVendor);

        webTestClient.get()
            .uri(Urls.CATEGORIES_BASE_URL + "/foo")
            .exchange()
            .expectStatus().isNotFound();

        System.out.println("- UPDATE");

        Vendor newVendor = Vendor.builder()
            .firstName("Foo")
            .lastName("Bar")
            .build();

        Vendor updatedVendor = webTestClient.put()
            .uri(Urls.VENDORS_BASE_URL + "/" + savedVendor.getId())
            .body(Mono.just(newVendor), vendor.getClass())
            .exchange()
            .expectStatus().isOk()
            .expectBody(Vendor.class).isEqualTo(newVendor)
            .consumeWith(this::printResult)
            .returnResult().getResponseBody();

        assertEquals(savedVendor.getId(), updatedVendor.getId());

        webTestClient.put()
            .uri(Urls.VENDORS_BASE_URL + "/foo")
            .body(Mono.just(newVendor), vendor.getClass())
            .exchange()
            .expectStatus().isNotFound();

        System.out.println("- DELETE");

        webTestClient.delete()
            .uri(Urls.VENDORS_BASE_URL + "/" + updatedVendor.getId())
            .exchange()
            .expectStatus().isOk();

        assertEquals(0, categoryRepository.count().block());

        webTestClient.delete()
            .uri(Urls.VENDORS_BASE_URL + "/foo")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void productCRUD() {

        Category category = Category.builder()
            .name("Best Seller")
            .build();
        category = categoryRepository.save(category).block();

        Vendor vendor = Vendor.builder()
            .firstName("John")
            .lastName("Doe")
            .build();
        vendor = vendorRepository.save(vendor).block();

        System.out.println("- CREATE");

        Product product = Product.builder()
            .name("Coffee Maker")
            .price("12.0")
            .vendorId(vendor.getId())
            .categoryId(category.getId())
            .build();

        Product savedProduct = webTestClient.post()
            .uri(Urls.PRODUCTS_BASE_URL)
            .body(Mono.just(product), product.getClass())
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Product.class).isEqualTo(product)
            .consumeWith(this::printResult)
            .returnResult().getResponseBody();

        assertEquals(product, savedProduct);

        Product badProduct = Product
            .builder()
            .name("a")
            .price("-1.0")
            .build();

        webTestClient.post()
            .uri(Urls.PRODUCTS_BASE_URL)
            .body(Mono.just(badProduct), Product.class)
            .exchange()
            .expectStatus().isBadRequest();

        Product badProduct2 = Product.builder()
            .name("Burgers")
            .price("6.0")
            .categoryId("badId")
            .vendorId(product.getVendorId())
            .build();

        webTestClient.post()
            .uri(Urls.PRODUCTS_BASE_URL)
            .body(Mono.just(badProduct2), Product.class)
            .exchange()
            .expectStatus().isNotFound();

        badProduct2.setCategoryId(product.getCategoryId());
        badProduct2.setVendorId("badId");

        webTestClient.post()
            .uri(Urls.PRODUCTS_BASE_URL)
            .body(Mono.just(badProduct2), Product.class)
            .exchange()
            .expectStatus().isNotFound();

        assertEquals(1, productRepository.count().block());

        System.out.println("- READ");

        Product foundProduct = webTestClient.get()
            .uri(Urls.PRODUCTS_BASE_URL + "/" + savedProduct.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody(Product.class).isEqualTo(savedProduct)
            .consumeWith(this::printResult)
            .returnResult().getResponseBody();

        assertEquals(savedProduct, foundProduct);

        webTestClient.get()
            .uri(Urls.PRODUCTS_BASE_URL + "/foo")
            .exchange()
            .expectStatus().isNotFound();

        System.out.println("- UPDATE");

        Product newProduct = Product.builder()
            .name("Coffee Maker")
            .price("15.0")
            .vendorId(product.getVendorId())
            .categoryId(product.getCategoryId())
            .build();

        Product updatedProduct = webTestClient.put()
            .uri(Urls.PRODUCTS_BASE_URL + "/" + savedProduct.getId())
            .body(Mono.just(newProduct), product.getClass())
            .exchange()
            .expectStatus().isOk()
            .expectBody(Product.class).isEqualTo(newProduct)
            .consumeWith(this::printResult)
            .returnResult().getResponseBody();

        webTestClient.put()
            .uri(Urls.PRODUCTS_BASE_URL + "/" + savedProduct.getId())
            .body(Mono.just(badProduct2), Product.class)
            .exchange()
            .expectStatus().isNotFound();

        badProduct2.setCategoryId("badId");
        badProduct2.setVendorId(product.getVendorId());

        webTestClient.put()
            .uri(Urls.PRODUCTS_BASE_URL + "/" + savedProduct.getId())
            .body(Mono.just(badProduct2), Product.class)
            .exchange()
            .expectStatus().isNotFound();

        assertEquals(savedProduct.getId(), updatedProduct.getId());

        webTestClient.put()
            .uri(Urls.PRODUCTS_BASE_URL + "/foo")
            .body(Mono.just(newProduct), product.getClass())
            .exchange()
            .expectStatus().isNotFound();

        System.out.println("- DELETE");

        webTestClient.delete()
            .uri(Urls.PRODUCTS_BASE_URL + "/" + updatedProduct.getId())
            .exchange()
            .expectStatus().isOk();

        assertEquals(0, productRepository.count().block());

        webTestClient.delete()
            .uri(Urls.PRODUCTS_BASE_URL + "/foo")
            .exchange()
            .expectStatus().isNotFound();
    }

    private void printResult(EntityExchangeResult<? extends Object> result) {
        System.out.println("Headers:");
        result.getResponseHeaders()
            .forEach((v, k) -> System.out.println("\t" + v + ": " + k));
        System.out.println("Body:");
        System.out.println("\t" + result.getResponseBody());
    }
}
