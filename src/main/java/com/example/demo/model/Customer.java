package com.example.demo.model;

import com.fasterxml.jackson.annotation.*;


import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String password;
    private String email;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference(value = "customerPurchase") // link the customer to the purchase details
    private Set<Purchases> purchases;

    public Set<Purchases> getPurchases() {
        return purchases;
    }
    public void setPurchases(Set<Purchases> purchases) {
        this.purchases = purchases;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String name) {
        this.password = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                Objects.equals(password, customer.password) &&
                Objects.equals(email, customer.email);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, password, email);
    }
    //TODO: uncomment this in production
//    @PostLoad
//    public void postLoad() {
//        this.password = null;
//    }
}
