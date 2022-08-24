package com.example.demo.controller;

import com.example.demo.exception.ResourcesNotFound;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public @ResponseBody Iterable <Customer> getAllCustomers() {
        //return only email and id
//        return customerRepository.findAll().stream().map(customer -> new CustomerView() {
//            @Override
//            public long getId() {
//              return   customer.getId();
//            }
//
//            @Override
//            public String getEmail() {
//                return customer.getEmail();
//            }
//
//            @Override
//            public Set<Purchases> getPurchases() {
//                return customer.getPurchases();
//            }
//        } ).collect(Collectors.toList());
        return customerRepository.findAll();
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

    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Customer> updateCustomer(@PathVariable long id, @RequestBody Customer cusomerUpdate) {
        Customer customerOrigins= customerRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Customer not found id: " + id));

        customerOrigins.setEmail(cusomerUpdate.getEmail());
        customerOrigins.setPassword(cusomerUpdate.getPassword());

        customerRepository.save(customerOrigins);

        return ResponseEntity.ok().body(customerOrigins);
    }


    @PostMapping(path = "/add")
    public @ResponseBody String addNewCustomer(@RequestBody Customer customer) {

        customerRepository.save(customer);
        return "Customer Created successfully";
    }
}
