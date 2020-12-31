package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.TestUtils;
import com.udacity.ecommerce.model.persistence.*;
import com.udacity.ecommerce.model.persistence.repositories.*;
import com.udacity.ecommerce.model.requests.ModifyCartRequest;
import org.junit.*;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static com.udacity.ecommerce.TestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    final Logger log = LoggerFactory.getLogger(CartControllerTest.class);

    private CartController cartController;


    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }


    @Test
    public void verify_add_to_cart() {
        ModifyCartRequest cartRequest = modifyCartRequest("Haben", 2l, 1);
        cartRequest.setItemId(2L);
        cartRequest.setUsername("test");
        cartRequest.setQuantity(1);

        User user = createUser(1,"Haben","pass1234");

        Item item = createItem(2, "Book", BigDecimal.valueOf(12), "new book");

        Cart cart = new Cart();
        cart.setId(3L);
        cart.addItem(item);
        cart.setTotal(BigDecimal.ONE);
        cart.setUser(user);

        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> cartResponse = cartController.addToCart(cartRequest);
        assertNotNull(cartResponse);
        assertEquals(200, cartResponse.getStatusCodeValue());

        Cart savedCart = cartResponse.getBody();
        assertNotNull(savedCart);
        assertEquals(cart, savedCart);
    }

    @Test
    public void verify_remove_from_cart() {

        User user = createUser(1,"Haben","pass1234");

        Item item = createItem(2, "Book", BigDecimal.valueOf(12), "new book");

        Cart cart = new Cart();
        cart.setId(3L);
        cart.addItem(item);
        cart.setTotal(BigDecimal.ONE);
        cart.setUser(user);

        user.setCart(cart);

        ModifyCartRequest cartRequest = modifyCartRequest("Haben", 2l, 3);

        when(userRepository.findByUsername("Haben")).thenReturn(user);
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> cartResponse = cartController.addToCart(cartRequest);
        assertNotNull(cartResponse);
        assertEquals(200, cartResponse.getStatusCodeValue());

        Cart savedCart = cartResponse.getBody();
        assertNotNull(savedCart);
        assertEquals(cart, savedCart);
        assertEquals(cart.getId(), savedCart.getId());
        assertEquals(cart.getUser(), savedCart.getUser());
        ResponseEntity<Cart> cartRemovedResponse = cartController.removeFromCart(cartRequest);
        assertNotNull(cartRemovedResponse);
        assertEquals(200, cartRemovedResponse.getStatusCodeValue());

        Cart cartRemoved = cartRemovedResponse.getBody();
        assertNotNull(cartRemoved);
        assertEquals(cart, cartRemoved);
        assertEquals(cart.getItems().stream().count(), cartRemoved.getItems().stream().count());
        assertEquals(cart.getUser(), cartRemoved.getUser());

        // Removing item not in cart
        ModifyCartRequest cartRemove = modifyCartRequest("Haben", 12l, 3);
        ResponseEntity<Cart>   notInCartRemovedResponse = cartController.removeFromCart(cartRemove);
        assertNotNull(notInCartRemovedResponse);

        Cart notInCartRemoved = notInCartRemovedResponse.getBody();
        assertNull(notInCartRemoved);
    }

}
