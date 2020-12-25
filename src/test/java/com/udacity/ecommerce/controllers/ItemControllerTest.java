package com.udacity.ecommerce.controllers;

import com.udacity.ecommerce.TestUtils;
import com.udacity.ecommerce.model.persistence.Item;
import com.udacity.ecommerce.model.persistence.repositories.ItemRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.udacity.ecommerce.TestUtils.*;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void verify_Get_All_Items() {

        List<Item> items = createItems();
        when(itemRepository.saveAll(items)).thenReturn(items);
        ResponseEntity<List<Item>> responseItems = itemController.saveAll(items);
        assertNotNull(responseItems);
        assertEquals(items, responseItems.getBody());
        assertSame(items, responseItems.getBody());

        List<Item> itemList =responseItems.getBody();
        assertNotNull(itemList);
        assertEquals(items.get(0).getName(),itemList.get(0).getName());
    }

    @Test
    public void verify_Get_Item_By_Id() {

        List<Item> items = createItems();
        when(itemRepository.saveAll(items)).thenReturn(items);
        ResponseEntity<List<Item>> responseAllItems=itemController.saveAll(items);
        assertNotNull(responseAllItems);
        assertEquals(items, responseAllItems.getBody());
        assertSame(items, responseAllItems.getBody());

        when(itemRepository.findById(1l)).thenReturn(Optional.of(items.get(0)));
        ResponseEntity<Item> response = itemController.getItemById(1l);
        Item savedItem = response.getBody();
        assertNotNull(savedItem);
        assertEquals(items.get(0).getName(),savedItem.getName());
    }
    @Test
    public void verify_Get_Items_By_Name() {

        List<Item> itemsInput = createItems();
        String name = itemsInput.get(0).getName();
        Predicate<Item> byName = item -> item.getName().equals(name);
        List<Item> itemByNameFromInput =itemsInput.stream().filter(byName).collect(Collectors.toList());

        when(itemRepository.saveAll(itemsInput)).thenReturn(itemsInput);
        ResponseEntity<List<Item>> responseAllItems=itemController.saveAll(itemsInput);
        assertNotNull(responseAllItems);
        assertEquals("Verify input items are saved ",itemsInput, responseAllItems.getBody());
        assertSame(itemsInput, responseAllItems.getBody());

        when(itemRepository.findByName(name)).thenReturn(Lists.newArrayList(itemByNameFromInput));
        ResponseEntity<List<Item>> response = itemController.getItemsByName(name);
        List<Item> savedItems = response.getBody();
        assertNotNull(savedItems);
        assertEquals(itemByNameFromInput,savedItems);
    }

    @Test
    public void verify_save_all(){
        List<Item> itemsInput = createItems();
        when(itemRepository.saveAll(itemsInput)).thenReturn(itemsInput);
        ResponseEntity<List<Item>> responseAllItems=itemController.saveAll(itemsInput);
        assertNotNull(responseAllItems);
        assertEquals("Verify input items are saved ",itemsInput, responseAllItems.getBody());
        assertSame(itemsInput, responseAllItems.getBody());
    }

}
