package com.example.demo.repository;

import com.example.demo.model.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasesRepository extends JpaRepository<Purchases, Long> {

}

