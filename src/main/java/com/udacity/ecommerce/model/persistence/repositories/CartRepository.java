package com.udacity.ecommerce.model.persistence.repositories;

import com.udacity.ecommerce.model.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
