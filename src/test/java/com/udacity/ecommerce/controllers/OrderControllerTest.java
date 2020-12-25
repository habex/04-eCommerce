package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.TestUtils;
import com.udacity.ecommerce.model.persistence.*;
import com.udacity.ecommerce.model.persistence.repositories.*;
import org.assertj.core.util.Lists;
import org.junit.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static com.udacity.ecommerce.TestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void verify_submit() {
        User user = createUser(1, "Haben", "pass1234");
        List<Item> items = createItems();
        Cart cart = new Cart();
        cart.setId(2l);
        cart.setUser(user);
        cart.setItems(items);
        user.setCart(cart);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<UserOrder> submitted = orderController.submit(user.getUsername());
        assertNotNull(submitted);

        User user_submit = submitted.getBody().getUser();
        List<Item> items_submit = submitted.getBody().getItems();
        assertEquals(user, user_submit);
        assertEquals(items, items_submit);
    }

    @Test
    public void verify_get_Orders_For_User() {
        String username = "Haben";
        User user = createUser(1, username, "pass1234");
        List<Item> items = createItems();

        UserOrder order = new UserOrder();
        order.setId(4l);
        order.setUser(user);
        order.setItems(items);
        order.setTotal(BigDecimal.ONE);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Lists.list(order));

        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser(username);
        assertNotNull(ordersForUser);
        assertEquals(1,ordersForUser.getBody().stream().count());
        assertEquals(Lists.list(order),ordersForUser.getBody());
    }

}
