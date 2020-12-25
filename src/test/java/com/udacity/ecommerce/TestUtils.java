package com.udacity.ecommerce;

import com.udacity.ecommerce.model.persistence.Item;
import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.requests.CreateUserRequest;
import com.udacity.ecommerce.model.requests.ModifyCartRequest;
import org.assertj.core.util.Lists;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

public class TestUtils {

    public static void injectObject(Object target,String fieldName,Object toBeInjected){
        boolean wasPrivate = false;
        try {
            Field field = target.getClass().getDeclaredField(fieldName);

            if(!field.isAccessible()){
                field.setAccessible(true);
                wasPrivate=true;
            }
            field.set(target,toBeInjected);
            if (wasPrivate){
                wasPrivate=false;
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static ModifyCartRequest modifyCartRequest(String username, long itemId, int quantity) {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername(username);
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(quantity);
        return cartRequest;
    }

    public static Item createItem(long id, String name, BigDecimal price, String description) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(price);
        item.setDescription(description);
        return item;
    }

    public static User createUser(long id, String username, String password) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    public static CreateUserRequest createUserRequest(String username, String password, String confirmPassword){
        CreateUserRequest cr = new CreateUserRequest();
        cr.setUsername(username);
        cr.setPassword(password);
        cr.setConfirmPassword(confirmPassword);
        return cr;
    }
    public static List<Item> createItems() {

        Item item1 = new Item();
        item1.setId(1l);
        item1.setName("Book");
        item1.setPrice(new BigDecimal(20));
        item1.setDescription("new book");
        Item item2 = new Item();
        item2.setId(2l);
        item2.setName("Laptop");
        item2.setPrice(new BigDecimal(30));
        item2.setDescription("HP");
        Item item3 = new Item();
        item3.setId(3l);
        item3.setName("Book");
        item3.setPrice(new BigDecimal(50));
        item3.setDescription("Java");

        List<Item> items = Lists.newArrayList(item1, item2,item3);
        return items;
    }
}
