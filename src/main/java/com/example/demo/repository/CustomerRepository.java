package com.example.demo.repository;

import com.example.demo.model.Customer;
import com.example.demo.projection.CustomerView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CustomerRepository extends JpaRepository<Customer, Long> {


}

