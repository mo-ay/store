package com.example.demo.model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;


@Entity
public class PurchaseDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "purches_id", referencedColumnName = "id",insertable = false,updatable = false)
    @JsonBackReference(value = "purchasesPurchaseDetails")
    private Purchases purchases;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id",insertable = false,updatable = false)
//    @JsonBackReference(value = "itemPurchaseDetails")
    private Item item;
    @Column(name = "purches_id")
    private Long purchaseId;
    @Column(name = "item_id")
    private Long itemId;
    private Long quantity;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Purchases getPurchases() {
        return purchases;
    }
    public void setPurchases(Purchases purchases) {
        this.purchases = purchases;
    }
    public Item getItem() {
        return item;
    }
    public void setItem(Item items) {
        this.item = items;
    }

    public Long getQuantity() {
        return quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "PurchaseDetails{" +
                "id=" + id +
                ", purchaseId=" + purchaseId +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseDetails that = (PurchaseDetails) o;
        return id == that.id &&
                Objects.equals(purchaseId, that.purchaseId) &&
                Objects.equals(itemId, that.itemId) &&
                Objects.equals(quantity, that.quantity);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, purchaseId, itemId, quantity);
    }
}
