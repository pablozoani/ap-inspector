package com.pzoani.supermarket.bootstrap;

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
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
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
        new Thread(() -> {
            try {
                Thread.sleep(3000l);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
            System.out.println("Loading data.");
            categoryRepository.deleteAll().block();
            Category[] categories = new Category[]{
                Category.builder().name("Best Offer").build(),
                Category.builder().name("High Quality").build(),
                Category.builder().name("Limited").build(),
                Category.builder().name("Best Seller").build()
            };
            for (int i = 0; i < categories.length; i++) {
                Category cat = categories[i];
                categories[i] = categoryRepository.save(categories[i]).block();
                System.out.println(cat.toString());
            }
            vendorRepository.deleteAll().block();
            Vendor[] vendors = new Vendor[]{
                Vendor.builder().firstName("Company").lastName(".ink").build(),
                Vendor.builder().firstName("Bar").lastName("Foo").build(),
                Vendor.builder().firstName("Maria").lastName("Bar").build()
            };
            for (int i = 0; i < vendors.length; i++) {
                Vendor vendor = vendors[i];
                vendors[i] = vendorRepository.save(vendor).block();
                System.out.println(vendor.toString());
            }
            categoryRepository.deleteAll().block();
            Product[] product_names = {
                Product.builder()
                    .name("Ink Cartridge")
                    .price(new BigDecimal(20.89))
                    .build(),
                Product.builder()
                    .name("Laptop")
                    .price(new BigDecimal(364.99))
                    .build(),
                Product.builder()
                    .name("Hard Disk Drive")
                    .price(new BigDecimal(43.99))
                    .build(),
                Product.builder()
                    .name("WiFi Router")
                    .price(new BigDecimal(59.99))
                    .build(),
                Product.builder()
                    .name("Led Monitor")
                    .price(new BigDecimal(158.00))
                    .build(),
                Product.builder()
                    .name("MicroSD")
                    .price(new BigDecimal(19.99))
                    .build(),
                Product.builder()
                    .name("LED Cooler")
                    .price(new BigDecimal(304.99))
                    .build(),
                Product.builder()
                    .name("Gaming Mouse")
                    .price(new BigDecimal(19.99))
                    .build(),
                Product.builder()
                    .name("Bluetooth Headphones")
                    .price(new BigDecimal(299.00))
                    .build(),
                Product.builder()
                    .name("Personal Computer")
                    .price(new BigDecimal(659.00))
                    .build(),
                Product.builder()
                    .name("Android Tablet")
                    .price(new BigDecimal(89.00))
                    .build(),
            };
            for (int i = 0; i < product_names.length; i++) {
                Product p = product_names[i];
                p.setVendor(vendors[i % vendors.length]);
                p.setCategory(categories[i % categories.length]);
                Product savedProduct = productRepository.save(p).block();
                System.out.println(savedProduct.toString());
            }
        }).start();
    }
}
