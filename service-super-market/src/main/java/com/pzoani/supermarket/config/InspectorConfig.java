package com.pzoani.supermarket.config;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.domain.Category;
import com.pzoani.supermarket.domain.Product;
import com.pzoani.supermarket.domain.Vendor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.pzoani.supermarket.exception.BadRequestException.exc;

@Configuration
public class InspectorConfig {

    @Bean
    public Inspector<Product> makeProductInspector() {
        return new Inspector<>(exc("Bad product state.")) {
            @Override
            public Product inspect(Product product) {
                return begin()
                    .isNotNull(exc("Product name cannot be null"),
                        product.getName()
                    ).run(() -> product.setName(product.getName().trim())
                    ).hasLengthRange(2, 64,
                        exc("The number of characters in the product " +
                            "name cannot be less than 2 or greater than 64."
                        ), product.getName()
                    ).isNotNull(exc("The price of the product cannot be null"),
                        product.getPrice()
                    ).isPositive(exc("The price of the product cannot be " +
                            "a negative number."
                        ), product.getPrice()
                    ).isNotBlank(exc("The category of the product cannot be " +
                        "null or blank."), product.getCategoryId()
                    ).isNotBlank(exc("The vendor of the product cannot be " +
                        "null or blank."), product.getVendorId()
                    ).end(product);
            }
        };
    }

    @Bean
    public Inspector<Category> makeCategoryInspector() {
        return new Inspector<>(exc("Bad category state.")) {
            @Override
            public Category inspect(Category category) {
                return begin()
                    .isNotNull(exc("Category name cannot be null."),
                        category.getName()
                    ).run(() -> category
                        .setName(category.getName().trim())
                    ).hasLengthRange(2, 64,
                        exc("The number of characters in the category name " +
                            "cannot be less than 2 or greater than 64."),
                        category.getName()
                    ).end(category);
            }
        };
    }

    @Bean
    public Inspector<Vendor> makeVendorInspector() {
        return new Inspector<>(exc("Bad vendor state.")) {
            @Override
            public Vendor inspect(Vendor v) {
                return begin()
                    .areNotNull(exc("Vendor's first and last name " +
                            "cannot be null."
                        ), v.getFirstName(), v.getLastName()
                    ).run(() -> {
                        v.setFirstName(v.getFirstName().trim());
                        v.setLastName(v.getLastName().trim());
                    }).haveLengthRange(2, 64,
                        exc("The number of characters in the vendor's " +
                            "first or last name " +
                            "cannot be less than 2 and greater than 64."
                        ), v.getFirstName(), v.getLastName()
                    ).end(v);
            }
        };
    }
}