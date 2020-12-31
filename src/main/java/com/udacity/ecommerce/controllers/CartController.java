package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.model.persistence.Cart;
import com.udacity.ecommerce.model.persistence.Item;
import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.ecommerce.model.persistence.repositories.ItemRepository;
import com.udacity.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.ecommerce.model.requests.ModifyCartRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private Logger log = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
        log.info("Adding to cart" + request.toString(), request);
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            log.error("User : " + request.getUsername() + " " + HttpStatus.NOT_FOUND.name());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Item> item = itemRepository.findById(request.getItemId());
        if (!item.isPresent()) {
            log.error("Item Id : " + request.getItemId() + " " + HttpStatus.NOT_FOUND.name());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();

        if (cart==null){
            log.warn("Cart fot not found" + user.getUsername() );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


        if (cart.getItems().stream().count() == 0) {
            log.warn("Item : " + item.get().getName() + " not found in cart.");
        }else {
            IntStream.range(0, request.getQuantity())
                    .forEach(i -> cart.addItem(item.get()));
            cartRepository.save(cart);
            log.info(cart.getItems().stream().count() +" "+item.get().getName()+ " added to cart.");
        }

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            log.error("User : " + request.getUsername() + " " + HttpStatus.NOT_FOUND.name());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Item> item = itemRepository.findById(request.getItemId());
        if (!item.isPresent()) {
            log.error("Item Id : " + request.getItemId() + " " + HttpStatus.NOT_FOUND.name());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Cart cart = user.getCart();

        if (cart==null){
            log.error("Cart fot not found for " + user.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (cart.getItems().stream().count() == 0) {
            log.warn("Item : " + item.get().getName() + " not found in cart.");
        }else {
            IntStream.range(0, request.getQuantity())
                    .forEach(i -> cart.removeItem(item.get()));
            cartRepository.save(cart);
            log.info(cart.getItems().stream().count() +" "+item.get().getName()+ " removed from cart.");
        }

        return ResponseEntity.ok(cart);
    }

}
