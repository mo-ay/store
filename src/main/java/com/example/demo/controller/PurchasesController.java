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


@Controller
@RequestMapping(path = "/purchases")
public class PurchasesController {
    private final PurchasesRepository purchasesRepository;
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
        try {
            Set<PurchaseDetails> purchaseDetails = purchases.getPurchaseDetails();
            purchases.setPurchaseDetails(null);
            purchasesRepository.save(purchases);
            final double[] amount = {0};
            purchaseDetails.forEach(purchaseDetail -> {
                //check if the item is present in the database
              Item item= itemRepository.findById(purchaseDetail.getItemId())
                      .orElseThrow(() ->
                              new ResourcesNotFound("Item not found id: " + purchaseDetail.getItemId()));
              //check if the item's quantity is greater than the quantity purchased
              if (item.getQuantity() < purchaseDetail.getQuantity()) {
                  throw new ResourcesNotFound("Not enough quantity for item id: " + purchaseDetail.getItemId());
              }
                //link the purchase to the purchase details
                purchaseDetail.setPurchaseId(purchases.getId());
                amount[0] += item.getPrice() * purchaseDetail.getQuantity();
                //update the item's quantity
                item.setQuantity(item.getQuantity() - purchaseDetail.getQuantity());
                itemRepository.save(item);
            });
            System.out.println("purchaseDetails: " + purchaseDetails);
            purchases.setAmount(amount[0]);
            purchases.setPurchaseDetails(purchaseDetails);
            purchasesRepository.save(purchases);

        }catch (Exception e) {
            System.out.println("Error while adding new purchases: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(purchases);
    }
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Purchases> deletePurchases(@PathVariable long id) {
        System.out.println("delete purchases");
        Purchases purchases= purchasesRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Purchases not found id: " + id));
        System.out.println("delete line");
        purchases.getCustomer().getPurchases().remove(purchases);
        customerController.updateCustomer(purchases.getCustomer().getId(), purchases.getCustomer());
        //not necessary!
        purchasesRepository.delete(purchases);
        return ResponseEntity.ok().body(purchases);
    }
    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Purchases> updatePurchases(@PathVariable long id, @RequestBody Purchases purchasesUpdate) {
        Purchases purchasesOrigins= purchasesRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Purchases not found id: " + id));
//        purchasesOrigins.setItems(purchasesUpdate.getItems());
//        purchasesOrigins.setCustomer(purchasesUpdate.getCustomer());
//        double amount = 0;
//        for (  Item i : purchasesUpdate.getItems()) {
//            amount += i.getPrice();
//        }
//        purchasesOrigins.setAmount(amount);
        purchasesOrigins.setDate(purchasesUpdate.getDate());
        //TODO: update purchases details
//        purchasesOrigins.setPurchaseDetails(purchasesUpdate.getPurchaseDetails());

        purchasesRepository.save(purchasesOrigins);
        return ResponseEntity.ok().body(purchasesOrigins);
    }
}
