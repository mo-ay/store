package com.example.demo.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
public class Item {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private long quantity;
    private double price;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "itemPurchaseDetails") // link the item to the purchase details
    private Set<PurchaseDetails> purchaseDetails;
//    @ManyToMany(mappedBy = "items")
//    @JsonIgnore
//    private Set<Purchases> purchases = new java.util.LinkedHashSet<>();

    public Set<PurchaseDetails> getPurchaseDetails() {
        return purchaseDetails;
    }
    public void setPurchaseDetails(Set<PurchaseDetails> purchaseDetails) {
        this.purchaseDetails = purchaseDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

//    public Set<Purchases> getPurchases() {
//        return purchases;
//    }
//
//    public void setPurchases(Set<Purchases> purchases) {
//        this.purchases = purchases;
//    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", purchaseDetails=" + purchaseDetails +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id &&
                quantity == item.quantity &&
                Double.compare(item.price, price) == 0 &&
                name.equals(item.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity, price);
    }

}
