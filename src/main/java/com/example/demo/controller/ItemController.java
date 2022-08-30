package com.example.demo.controller;

import com.example.demo.exception.ResourcesNotFound;
import com.example.demo.model.Item;
import com.example.demo.projection.ItemDTO;
import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(path = "/item")
public class ItemController {
    //inject ItemRepository
    private final ItemRepository itemRepository;
    @Autowired
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    //get all items
    @GetMapping(path = "/all")
    public @ResponseBody Iterable <ItemDTO> getAllItems() {

       return itemRepository.findAll().stream().map(item -> new ItemDTO() {
             @Override
             public Long getId() {
                 return item.getId();
             }

             @Override
             public String getName() {
                 return item.getName();
             }

             @Override
             public Long getQuantity() {
                 return item.getQuantity();
             }

             @Override
             public Double getPrice() {
                 return item.getPrice();
             }
         }).collect(Collectors.toList());
    }
    //get item by id
    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Item> getItem(@PathVariable long id) {
        Item item= itemRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Item not found id: " + id));
        return ResponseEntity.ok().body(item);
    }
    //delete item by id
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Item> deleteItem(@PathVariable long id) {
        Item item= itemRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Item not found id: " + id));
        try {
            itemRepository.delete(item);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(item);
        }

        return ResponseEntity.ok().body(item);
    }
    //update item by id
    @PutMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<Item> updateItem(@PathVariable long id, @RequestBody Item itemUpdate) {
        Item itemOrigins= itemRepository.findById(id).
                orElseThrow(() -> new ResourcesNotFound("Item not found id: " + id));

        itemOrigins.setName(itemUpdate.getName());
        itemOrigins.setPrice(itemUpdate.getPrice());
        itemOrigins.setQuantity(itemUpdate.getQuantity());

        itemRepository.save(itemOrigins);
        return ResponseEntity.ok().body(itemOrigins);
    }
    //add new item
    @PostMapping(path = "/add")
    public @ResponseBody ResponseEntity<Item> addNewItem(@RequestBody Item item) {
        itemRepository.save(item);
        return ResponseEntity.ok().body(item);
    }

}
