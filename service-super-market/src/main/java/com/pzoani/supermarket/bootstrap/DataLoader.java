package com.pzoani.supermarket.bootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.domain.Product;
import com.pzoani.supermarket.domain.Vendor;
import com.pzoani.supermarket.repository.CategoryRepository;
import com.pzoani.supermarket.repository.ProductRepository;
import com.pzoani.supermarket.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("bootstrap")
@Component
public class DataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;

    @Autowired
    public DataLoader(CategoryRepository categoryRepository,
        VendorRepository vendorRepository, ProductRepository productRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        loadData();
    }

    private void loadData() {
        new Thread(() -> {
            try {

                // w8 4 db
                Thread.sleep(3000L);
                ObjectMapper objectMapper = new ObjectMapper();
                System.out.println("Loading data.");

                categoryRepository.deleteAll().block();
                Category[] categories = new Category[]{
                    Category.builder().name("Best Offer").build(),
                    Category.builder().name("High Quality").build(),
                    Category.builder().name("Limited").build(),
                    Category.builder().name("Best Seller").build()
                };

                for (int i = 0; i < categories.length; i++) {
                    Category category = categories[i];
                    categories[i] =
                        categoryRepository.save(categories[i]).block();
                    System.out.println(objectMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(category)
                    );
                }

                vendorRepository.deleteAll().block();
                Vendor[] vendors = new Vendor[]{
                    Vendor.builder().firstName("Company")
                        .lastName(".ink").build(),
                    Vendor.builder().firstName("Bar")
                        .lastName("Foo").build(),
                    Vendor.builder().firstName("Maria")
                        .lastName("Bar").build()
                };

                for (int i = 0; i < vendors.length; i++) {
                    Vendor vendor = vendors[i];
                    vendors[i] = vendorRepository.save(vendor).block();
                    System.out.println(objectMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(vendor)
                    );
                }

                categoryRepository.deleteAll().block();
                Product[] products = {
                    Product.builder().name("Ink Cartridge")
                        .price(20.89).build(),
                    Product.builder().name("Laptop")
                        .price(364.99).build(),
                    Product.builder().name("Hard Disk Drive")
                        .price(43.99).build(),
                    Product.builder().name("WiFi Router")
                        .price(59.99).build(),
                    Product.builder().name("Led Monitor")
                        .price(158.00).build(),
                    Product.builder().name("MicroSD")
                        .price(19.99).build(),
                    Product.builder().name("LED Cooler")
                        .price(304.99).build(),
                    Product.builder().name("Gaming Mouse")
                        .price(19.99).build(),
                    Product.builder().name("Bluetooth Headphones")
                        .price(299.00).build(),
                    Product.builder().name("Personal Computer")
                        .price(659.00).build(),
                    Product.builder().name("Android Tablet")
                        .price(89.00).build(),
                };

                productRepository.deleteAll().block();

                for (int i = 0; i < products.length; i++) {
                    Product p = products[i];
                    p.setVendorId(vendors[i % vendors.length].getId());
                    p.setCategoryId(categories[i % categories.length].getId());
                    Product savedProduct = productRepository.save(p).block();
                    System.out.println(objectMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(savedProduct)
                    );
                }

            } catch (InterruptedException | JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            } finally {
                System.out.println("...");
            }
        }).start();
    }
}
