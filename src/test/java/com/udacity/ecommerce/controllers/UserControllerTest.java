package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.TestUtils;
import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.repositories.*;
import com.udacity.ecommerce.model.requests.CreateUserRequest;
import org.junit.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.udacity.ecommerce.TestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("pass1234")).thenReturn("pass");
        CreateUserRequest cr = createUserRequest("Haben","pass1234","pass1234");

        final ResponseEntity<User> response = userController.createUser(cr);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals("Haben", user.getUsername());
        assertEquals("pass", user.getPassword());
    }


    @Test
    public void verify_get_user_by_id(){

        when(encoder.encode("pass1234")).thenReturn("thisIsHashed");

        CreateUserRequest cr = createUserRequest("Habex","pass1234","pass1234");

        final ResponseEntity<User> response = userController.createUser(cr);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals("Habex", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        final ResponseEntity<User> response_by_Id = userController.findById(user.getId());
        assertNotNull(response_by_Id);
        assertEquals(200, response_by_Id.getStatusCodeValue());

        User user_by_Id = response_by_Id.getBody();
        assertNotNull(user_by_Id);
        assertEquals("Habex", user_by_Id.getUsername());
        assertEquals("thisIsHashed", user_by_Id.getPassword());

    }

    @Test
    public void verify_get_user_by_name(){

        when(encoder.encode("pass1234")).thenReturn("thisIsHashed");

        CreateUserRequest cr = createUserRequest("Habex","pass1234","pass1234");

        final ResponseEntity<User> response = userController.createUser(cr);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals("Habex", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        final ResponseEntity<User> response_by_name = userController.findByUserName(user.getUsername());
        assertNotNull(response_by_name);
        assertEquals(200, response_by_name.getStatusCodeValue());

        User user_by_name = response_by_name.getBody();

        assertNotNull(user_by_name);
        assertEquals("Habex", user_by_name.getUsername());
        assertEquals("thisIsHashed", user_by_name.getPassword());
    }


}
