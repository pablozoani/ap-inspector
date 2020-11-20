package com.pzoani.supermarket.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Vendor {

    @Id
    private String id;
    private String firstName;
    private String lastName;

    public Vendor() {
    }

    public Vendor(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        Vendor vendor = (Vendor) obj;
        return this.firstName.equals(vendor.firstName) &&
            this.lastName.equals(vendor.lastName);
    }

    @Override
    public int hashCode() {
        return this.lastName.hashCode() + this.firstName.hashCode();
    }

    @Override
    public String toString() {
        return "Vendor{" +
            "id='" + id + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String firstName;
        private String lastName;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Vendor build() {
            return new Vendor(id, firstName, lastName);
        }
    }
}
