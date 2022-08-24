package com.example.demo.repository;


import com.example.demo.model.PurchaseDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasesDetailsRepository extends JpaRepository<PurchaseDetails, Long> {

}

