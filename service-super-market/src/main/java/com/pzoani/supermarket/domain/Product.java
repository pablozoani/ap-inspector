package com.pzoani.supermarket.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Objects;

@Document
public class Product {

    @Id
    private String id;

    private String name;

    private BigDecimal price;

    @DBRef
    private Category category;

    @DBRef
    private Vendor vendor;

    public Product() {
    }

    public Product(String id, String name, BigDecimal price,
        Category category, Vendor vendor
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.vendor = vendor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product that = (Product) obj;
        return name.equals(that.name) &&
            price.equals(that.price) &&
            vendor.equals(that.vendor) &&
            category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category, vendor);
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        return "Product{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", category=" + category +
            ", vendor=" + vendor +
            '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;

        private BigDecimal price;

        private Category category;

        private Vendor vendor;

        public Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder vendor(Vendor vendor) {
            this.vendor = vendor;
            return this;
        }

        public Product build() {
            return new Product(null, name, price,
                category, vendor
            );
        }
    }
}
