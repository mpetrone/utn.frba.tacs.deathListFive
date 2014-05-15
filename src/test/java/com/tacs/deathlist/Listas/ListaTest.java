package com.tacs.deathlist.Listas;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.LinkedList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.tacs.deathlist.domain.CustomForbiddenException;
import com.tacs.deathlist.domain.Item;
import com.tacs.deathlist.domain.Lista;

public class ListaTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testCompararListas() {
		
		Lista lista1 = new Lista("Lenguajes");
		lista1.agregarItem("Python");
		lista1.agregarItem("Ruby");
		
		Lista lista2 = new Lista("Lenguajes");
		lista2.agregarItem("Java");
		lista2.agregarItem("Scala");
		
		// las listas se comparan por el nombre
		assertEquals(lista1, lista2);
	}
	
	
	@Test
    public void testLimitarItems() {
    	Lista lista = new Lista("Numeros");
    	
    	// se llena la lista
    	for(int i=1; i<=lista.getMaxItems(); i++) {
    		lista.agregarItem(String.valueOf(i));
    	}
    	
    	// agregar un item mas deberia lanzar excepcion
    	exception.expect(CustomForbiddenException.class);
    	lista.agregarItem("itemExtra");
    }
	
	
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
