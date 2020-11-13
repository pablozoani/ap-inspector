package com.pzoani.supermarket.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Category {

    @Id
    private String id;

    private String description;

    public Category() {
    }

    public Category(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Category cat = (Category) obj;
        return this.description.equals(cat.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    @Override
    public String toString() {
        return "Category: {\n id: " + id + ",\n description: " + description + "\n}";
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
            output.description = description;
            return output;
        }
    }
}
