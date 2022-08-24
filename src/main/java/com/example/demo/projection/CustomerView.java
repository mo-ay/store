package com.example.demo.projection;

import com.example.demo.model.Purchases;

import java.util.Set;

public interface CustomerView {
    long getId();
    String getEmail();
    Set<Purchases> getPurchases();
}
