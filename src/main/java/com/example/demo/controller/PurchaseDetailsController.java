package com.example.demo.controller;

import com.example.demo.exception.ResourcesNotFound;
import com.example.demo.model.PurchaseDetails;
import com.example.demo.repository.PurchasesDetailsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(path = "/purchase_details")
public class PurchaseDetailsController {
    private final PurchasesDetailsRepository detailsRepository;
    public PurchaseDetailsController(PurchasesDetailsRepository detailsRepository) {
        this.detailsRepository = detailsRepository;
    }
    @GetMapping(path = "/all")
    public @ResponseBody Iterable <PurchaseDetails> getAllPurchaseDetails() {
        return detailsRepository.findAll();
    }
    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<PurchaseDetails> getPurchaseDetails(@PathVariable long id) {
        PurchaseDetails purchaseDetails= detailsRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Order not found id: " + id));
        return ResponseEntity.ok().body(purchaseDetails);
    }
    //TODO: check if the field purchase for the order is correct
    @PostMapping(path = "/add")
    public @ResponseBody String addNewPurchaseDetails(@RequestBody PurchaseDetails purchaseDetails) {
        detailsRepository.save(purchaseDetails);
        return "purchaseDetails added successfully";
    }
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<PurchaseDetails> deletePurchaseDetails(@PathVariable long id) {
        PurchaseDetails purchaseDetails= detailsRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Order not found id: " + id));
        detailsRepository.delete(purchaseDetails);
        return ResponseEntity.ok().body(purchaseDetails);
    }
    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<PurchaseDetails> updatePurchaseDetails(@PathVariable long id, @RequestBody PurchaseDetails purchaseDetailsUpdate) {
        PurchaseDetails purchaseDetailsOrigins= detailsRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Order not found id: " + id));
        purchaseDetailsOrigins.setPurchases(purchaseDetailsUpdate.getPurchases());
        purchaseDetailsOrigins.setQuantity(purchaseDetailsUpdate.getQuantity());
        purchaseDetailsOrigins.setItem(purchaseDetailsUpdate.getItem());
        detailsRepository.save(purchaseDetailsUpdate);
        return ResponseEntity.ok().body(purchaseDetailsOrigins);
    }

}
