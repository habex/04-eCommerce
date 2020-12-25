package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.model.persistence.*;
import com.udacity.ecommerce.model.persistence.repositories.*;
import com.udacity.ecommerce.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private Logger log = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		log.info("Creating user {}", createUserRequest.getUsername());

		String username = createUserRequest.getUsername();
		String password = createUserRequest.getPassword();
		String confirmPassword = createUserRequest.getConfirmPassword();

		User user = new User();
		user.setUsername(username);
		Cart cart = new Cart();
		user.setCart(cart);
		if (password.length() < 7 ||
				!password.equals(confirmPassword)) {
			log.error("Error with user password. Cannot create user {}", username);
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(bCryptPasswordEncoder.encode(password));
		cartRepository.save(cart);
		userRepository.save(user);
		log.info("Saved user " + username,userRepository.findByUsername(username));
		return ResponseEntity.ok(user);
	}
	
}
