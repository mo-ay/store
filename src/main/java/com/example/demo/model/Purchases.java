package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class Purchases {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false,updatable = false)
    @JsonBackReference // to avoid circular reference in json response (customer -> purchases -> customer)
    //@JsonBackReference is better than @JsonIgnore because it is the way to go.
    private Customer customer;

    @Column(name = "customer_id")
    private long customerId;
//    @OneToMany(mappedBy = "purchases", cascade = CascadeType.ALL)
//    private Set<PurchaseDetails> purchaseDetails;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinTable(name = "purchase_details",
            joinColumns = @JoinColumn(name = "purches_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"purches_id", "item_id"}))
    private Set<Item> items = new java.util.LinkedHashSet<>();
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    private Date date;
    private double amount;

    public Set<Item> getItems() {
        return items;
    }
    public void setItems(Set<Item> items) {
        this.items = items;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

//    public Set<PurchaseDetails> getPurchaseDetails() {
//        return purchaseDetails;
//    }
//    public void setPurchaseDetails(Set<PurchaseDetails> purchaseDetails) {
//        this.purchaseDetails = purchaseDetails;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Purchases{" +
                "id=" + id +
                ", customer=" + customer +
                ", items=" + items +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchases purchases = (Purchases) o;
        return id == purchases.id &&
                customerId == purchases.customerId &&
                Double.compare(purchases.amount, amount) == 0 &&
                Objects.equals(customer, purchases.customer) &&
                Objects.equals(items, purchases.items) &&
                Objects.equals(date, purchases.date);
    }
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, customer, items, date, amount);
//    }

}
