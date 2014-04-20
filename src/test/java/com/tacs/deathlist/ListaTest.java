package com.tacs.deathlist;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tacs.deathlist.domain.Item;
import com.tacs.deathlist.domain.Lista;

public class ListaTest {

    private static Lista lista;
    
    @Test
    public void testAgregarItems() {    	
    	
    	lista = new Lista("Paises");
    	
    	lista.agregarItem("Argentina");
    	lista.agregarItem("Chile");
    	lista.agregarItem("Uruguay");
    	lista.agregarItem("Brasil");
    	lista.agregarItem("Rusia");
    	lista.agregarItem("Argentina");
    	lista.agregarItem("Chile");
    	
        assertEquals(7, lista.size());
        
        assertTrue(lista.existeElItemConNombre("Uruguay"));
        assertTrue(lista.existeElItemConNombre("Chile"));
        assertFalse(lista.existeElItemConNombre("Colombia"));
    }
    
    @Test
    public void testVotarItems() {
    	
    	lista = new Lista("Paises");
    	
    	Item itemArg = new Item("Argentina");
    	Item itemUru = new Item("Uruguay");
    	Item itemChi = new Item("Chile");
    	
    	lista.agregarItem("Chile");
    	lista.agregarItem("Uruguay");
    	lista.agregarItem("Argentina");
    	
    	for (int i = 1; i <= 9; i++)
    		lista.votarItem("Argentina");
    	
    	assertEquals(0, lista.posicionDelItem(itemArg));    	
    	
    	for (int i = 1; i <= 2; i++)
    		lista.votarItem("Chile");
    	
    	lista.votarItem("Uruguay");
    	
        assertEquals(0, lista.posicionDelItem(itemArg));
        assertEquals(1, lista.posicionDelItem(itemChi));
        assertEquals(2, lista.posicionDelItem(itemUru));
        
        for (int i = 1; i <= 5; i++)
    		lista.votarItem("Uruguay");
        
        assertEquals(0, lista.posicionDelItem(itemArg));
        assertEquals(1, lista.posicionDelItem(itemUru));
        assertEquals(2, lista.posicionDelItem(itemChi));
        
        for (int i = 1; i <= 20; i++)
    		lista.votarItem("Uruguay");
        
        assertEquals(0, lista.posicionDelItem(itemUru));
        assertEquals(1, lista.posicionDelItem(itemArg));
        assertEquals(2, lista.posicionDelItem(itemChi));
        
        assertEquals(3, lista.size());
        
    }
}
