package com.example.demo.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.util.Set;


@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String password;
    private String email;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference // link the customer to the purchase details
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
                ", email='" + email + '\'' +
                ", purchases=" + purchases +
                '}';
    }
    //hashcode and equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                email.equals(customer.email);
    }
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
//    @PostLoad
//    public void postLoad() {
//        this.password = null;
//    }
}
