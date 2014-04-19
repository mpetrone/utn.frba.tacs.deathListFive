package com.tacs.deathlist;

import static org.junit.Assert.*;

import org.junit.Test;

public class ListaInMemoryTest {

    private static ListaInMemory lista;
    
    @Test
    public void testAgregarItems() {    	
    	
    	lista = new ListaInMemory("Paises");
    	
    	lista.agregarItem("Argentina");
    	lista.agregarItem("Chile");
    	lista.agregarItem("Uruguay");
    	lista.agregarItem("Brasil");
    	lista.agregarItem("Rusia");
    	lista.agregarItem("Argentina");
    	lista.agregarItem("Chile");
    	
        assertEquals(5, lista.size());
        
        assertTrue(lista.existeElItemConNombre("Uruguay"));
        assertTrue(lista.existeElItemConNombre("Chile"));
        assertFalse(lista.existeElItemConNombre("Colombia"));
    }
    
    @Test
    public void testVotarItems() {
    	
    	lista = new ListaInMemory("Paises");
    	
    	ItemInMemory itemArg = new ItemInMemory("Argentina");
    	ItemInMemory itemUru = new ItemInMemory("Uruguay");
    	ItemInMemory itemChi = new ItemInMemory("Chile");
    	
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
