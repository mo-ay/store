package com.example.demo.controller;

import com.example.demo.exception.ResourcesNotFound;
import com.example.demo.model.Customer;
import com.example.demo.projection.CustomerDto;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(path = "/customer")
public class CustomerController {
    private final CustomerRepository customerRepository;
    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable <CustomerDto> getAllCustomers() {
       // return only email and id
        return customerRepository.findAll().stream().map(customer -> new CustomerDto() {
            @Override
            public long getId() {
              return   customer.getId();
            }

            @Override
            public String getEmail() {
                return customer.getEmail();
            }
        } ).collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Customer> getCustomer(@PathVariable long id) {
        Customer customer= customerRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Customer not found id: " + id));
        return ResponseEntity.ok().body(customer);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Customer> deleteCustomer(@PathVariable long id) {
        Customer customer= customerRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Customer not found id: " + id));
        customerRepository.delete(customer);
        return ResponseEntity.ok().body(customer);
    }
    //get all costumers with purchase history
    //TODO: remove this method and create a new controller for admin
    @GetMapping(path = "/forAdmin")
    public @ResponseBody Iterable <Customer> getAllCustomersWithPurchaseHistory() {
        return customerRepository.findAll();
    }



    //customer can only update email and password
    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Customer> updateCustomer(@PathVariable long id, @RequestBody Customer customerUpdate) {
        Customer customerOrigins= customerRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Customer not found id: " + id));

        customerOrigins.setEmail(customerUpdate.getEmail());
        customerOrigins.setPassword(customerUpdate.getPassword());

        customerRepository.save(customerOrigins);

        return ResponseEntity.ok().body(customerOrigins);
    }
    //helper method to delete purchase history of customer
    public @ResponseBody ResponseEntity<Customer> updateCustomerPurchase(@PathVariable long id, @RequestBody Customer customerUpdate) {
        Customer customerOrigins= customerRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Customer not found id: " + id));

        customerOrigins.setEmail(customerUpdate.getEmail());
        customerOrigins.setPassword(customerUpdate.getPassword());
        customerOrigins.setPurchases(customerUpdate.getPurchases());
        customerRepository.save(customerOrigins);

        return ResponseEntity.ok().body(customerOrigins);
    }


    @PostMapping(path = "/add")
    public @ResponseBody ResponseEntity<Customer> addNewCustomer(@RequestBody Customer customer) {

        customerRepository.save(customer);
        return ResponseEntity.ok().body(customer);
    }
}
