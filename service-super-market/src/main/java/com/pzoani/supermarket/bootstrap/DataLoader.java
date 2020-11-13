package com.pzoani.supermarket.bootstrap;

import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.domain.Vendor;
import com.pzoani.supermarket.repository.CategoryRepository;
import com.pzoani.supermarket.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class DataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private final CategoryRepository categoryRepository;

    private final VendorRepository vendorRepository;

    @Autowired
    public DataLoader(CategoryRepository categoryRepository,
        VendorRepository vendorRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println(" ### DATA LOADING ### ");

        categoryRepository.deleteAll().block();
        Category cat1 = Category.builder().description("Fruit").build(),
            cat2 = Category.builder().description("Vegetable").build(),
            cat3 = Category.builder().description("Dairy").build();
        categoryRepository.saveAll(Flux.just(cat1, cat2, cat3)).blockLast();
        System.out.println(" ### " + categoryRepository.count().block() +
            " CATEGORIES LOADED ###");

        vendorRepository.deleteAll().block();
        Vendor vendor1 = Vendor.builder().firstName("John").lastName("Doe").build(),
            vendor2 = Vendor.builder().firstName("Pablo").lastName("Foo").build(),
            vendor3 = Vendor.builder().firstName("Maria").lastName("Bar").build();
        vendorRepository.saveAll(Flux.just(vendor1, vendor2, vendor3)).blockLast();
        System.out.println(" ### " + vendorRepository.count().block() +
            " VENDORS LOADED ###");
    }
}
