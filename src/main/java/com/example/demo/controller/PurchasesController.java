package com.example.demo.controller;

import com.example.demo.exception.ResourcesNotFound;
import com.example.demo.model.Item;
import com.example.demo.model.PurchaseDetails;
import com.example.demo.model.Purchases;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.PurchasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(path = "/purchases")
public class PurchasesController {
    private final PurchasesRepository purchasesRepository;
    //this is not a good practice to inject controller into another controller
    //TODO: inject repository into controller instead, Controller considered as presenter!
    private final CustomerController customerController;
    private final ItemRepository itemRepository;
    @Autowired
    public PurchasesController(PurchasesRepository purchasesRepository, CustomerController customerController, ItemRepository itemController) {
        this.purchasesRepository = purchasesRepository;
        this.customerController = customerController;
        this.itemRepository = itemController;
    }
    @GetMapping(path = "/all")
    public @ResponseBody Iterable <Purchases> getAllPurchases() {
        return purchasesRepository.findAll();
    }
    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Purchases> getPurchases(@PathVariable long id) {
        System.out.println("retrieving purchases with id: " + id);
        Purchases purchases= purchasesRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Purchases not found id: " + id));
        return ResponseEntity.ok().body(purchases);
    }
    @PostMapping(path = "/add")
    public @ResponseBody ResponseEntity<Purchases > addNewPurchases(@RequestBody Purchases purchases) {


            if (customerController.getCustomer(purchases.getCustomerId()).getStatusCode() != HttpStatus.OK ) {
                throw new ResourcesNotFound("Customer not found id: " + purchases.getCustomerId());
            }

            Set<PurchaseDetails> purchaseDetails = purchases.getPurchaseDetails();
            purchases.setPurchaseDetails(null);
            if (purchaseDetails==null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            purchasesRepository.save(purchases);
            final double[] amount = {0};
            purchaseDetails.forEach(purchaseDetail -> {
                //check if the item is present in the database
              Item item= itemRepository.findById(purchaseDetail.getItemId()).orElse(null);
              if (item==null) {
                  //must be cleaned up unsuccessful purchase
                  deletePurchases(purchases.getId());
                  throw  new ResourcesNotFound("Item not found id: " + purchaseDetail.getItemId());
              }
              //check if the item's quantity is greater than the quantity purchased
              if (item.getQuantity() < purchaseDetail.getQuantity()) {
                  //must be cleaned up unsuccessful purchase
                  deletePurchases(purchases.getId());
                  throw new ResourcesNotFound("Not enough quantity for item id: " + purchaseDetail.getItemId());
              }
                //link the purchase to the purchase details
                purchaseDetail.setPurchaseId(purchases.getId());
                amount[0] += item.getPrice() * purchaseDetail.getQuantity();
                //update the item's quantity
                item.setQuantity(item.getQuantity() - purchaseDetail.getQuantity());
                itemRepository.save(item);
            });
            purchases.setAmount(amount[0]);
            purchases.setPurchaseDetails(purchaseDetails);
            purchasesRepository.save(purchases);


        return ResponseEntity.status(HttpStatus.CREATED).body(purchases);
    }
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Purchases> deletePurchases(@PathVariable long id) {
        System.out.println("delete purchases");
        Purchases purchases= purchasesRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Purchases not found id: " + id));
        System.out.println("delete line");
        purchases.getCustomer().getPurchases().remove(purchases);
        customerController.updateCustomerPurchase(purchases.getCustomer().getId(), purchases.getCustomer());
        //not necessary!
        purchasesRepository.delete(purchases);
        return ResponseEntity.ok().body(purchases);
    }
    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Purchases> updatePurchases(@PathVariable long id, @RequestBody Purchases purchasesUpdate) {
        Purchases purchasesOrigins= purchasesRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Purchases not found id: " + id));

        //TODO: update purchases details like the add method without the cleaning process
        purchasesOrigins.setPurchaseDetails(purchasesUpdate.getPurchaseDetails());

        purchasesRepository.save(purchasesOrigins);
        return ResponseEntity.ok().body(purchasesOrigins);
    }
}
