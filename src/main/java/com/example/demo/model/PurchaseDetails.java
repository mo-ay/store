//package com.example.demo.model;
//import javax.persistence.*;
//
//
//@Entity
//public class PurchaseDetails {
//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    private long id;
//    @ManyToOne
//    @JoinColumn(name = "purches_id", referencedColumnName = "id")
//    private Purchases purchases;
//    @ManyToOne
//    @JoinColumn(name = "item_id", referencedColumnName = "id")
//    private Item item;
//    private long quantity;
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public Purchases getPurchases() {
//        return purchases;
//    }
//    public void setPurchases(Purchases purchases) {
//        this.purchases = purchases;
//    }
//    public Item getItem() {
//        return item;
//    }
//    public void setItem(Item items) {
//        this.item = items;
//    }
//
//    public long getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(long quantity) {
//        this.quantity = quantity;
//    }
//
//    @Override
//    public String toString() {
//        return "Order{" +
//                "id=" + id +
//                ", purchases=" + purchases +
//                ", items=" + item +
//                ", quantity=" + quantity +
//                '}';
//    }
//}
