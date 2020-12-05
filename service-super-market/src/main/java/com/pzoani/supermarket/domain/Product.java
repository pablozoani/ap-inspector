package com.pzoani.supermarket.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.Objects;

@Document
public class Product {

    @Id
    private String id;
    private String name;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;
    private String categoryId;
    private String vendorId;

    public Product() {
    }

    public Product(String id, String name, String price,
        String categoryId, String vendorId
    ) {
        this(id, name, new BigDecimal(price), categoryId, vendorId);
    }

    public Product(String id, String name, BigDecimal price,
        String categoryId, String vendorId
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.vendorId = vendorId;
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

    public void setPrice(String price) {
        this.price = new BigDecimal(price);
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Product that = (Product) obj;
        return name.equals(that.name) &&
            price.equals(that.price) &&
            vendorId.equals(that.vendorId) &&
            categoryId.equals(that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, categoryId, vendorId);
    }

    @Override
    public String toString() {
        return "Product{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", category=" + categoryId +
            ", vendor=" + vendorId +
            '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private BigDecimal price;
        private String categoryId;
        private String vendorId;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder price(String price) {
            this.price = new BigDecimal(price);
            return this;
        }

        public Builder categoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder vendorId(String vendorId) {
            this.vendorId = vendorId;
            return this;
        }

        public Product build() {
            return new Product(id, name, price,
                categoryId, vendorId
            );
        }
    }
}
