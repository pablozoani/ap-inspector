package com.pzoani.supermarket.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Category {

    @Id
    private String id;

    private String name;

    public Category() {
    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Category cat = (Category) obj;
        return this.name.equals(cat.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Category{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String description = null;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Category build() {
            Category output = new Category();
            output.name = description;
            return output;
        }
    }
}
