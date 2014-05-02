package com.tacs.deathlist.Listas;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.LinkedList;

import org.junit.Test;

import com.tacs.deathlist.domain.Item;
import com.tacs.deathlist.domain.Lista;

public class ListaTest {

    
    @Test
    public void testVotarItems() throws Exception {
        // hacemos accesible la propiedad items para testear
        Field itemsField = Lista.class.getDeclaredField("items");
        itemsField.setAccessible(true);
    	
        Lista lista = new Lista("Paises");
    	@SuppressWarnings("unchecked")
    	LinkedList<Item> items = (LinkedList<Item>) itemsField.get(lista);
    	
    	Item itemArg = new Item("Argentina");
    	Item itemUru = new Item("Uruguay");
    	Item itemChi = new Item("Chile");
    	
    	lista.agregarItem("Chile");
    	lista.agregarItem("Uruguay");
    	lista.agregarItem("Argentina");
    	
    	assertEquals(3, items.size());
    	
    	for (int i = 1; i <= 9; i++)
    		lista.votarItem("Argentina");
    	
    	assertEquals("Argentina deberia ir primero", itemArg, items.get(0));    	
    	
    	for (int i = 1; i <= 2; i++)
    		lista.votarItem("Chile");
    	lista.votarItem("Uruguay");
    	
    	assertEquals("Argentina deberia ir primero", itemArg, items.get(0));
    	assertEquals("Chile deberia ir segundo", itemChi, items.get(1));
    	assertEquals("Uruguay deberia ir ultimo", itemUru, items.get(2));
        
        for (int i = 1; i <= 5; i++)
    		lista.votarItem("Uruguay");
        
        assertEquals("Argentina deberia ir primero", itemArg, items.get(0));
        assertEquals("Uruguay deberia ir segundo", itemUru, items.get(1));
        assertEquals("Chile deberia ir ultimo", itemChi, items.get(2));
        
        for (int i = 1; i <= 20; i++)
    		lista.votarItem("Uruguay");
        
        assertEquals("Uruguay deberia ir primero", itemUru, items.get(0));
        assertEquals("Argentina deberia ir segundo", itemArg, items.get(1));
        assertEquals("Chile deberia ir ultimo", itemChi, items.get(2));
    }
}
