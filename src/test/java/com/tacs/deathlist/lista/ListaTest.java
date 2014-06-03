package com.tacs.deathlist.lista;

import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class ListaTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testCompararListas() {
		
		Lista lista1 = new Lista("1234", "Lenguajes");
		lista1.agregarItem("Python");
		lista1.agregarItem("Ruby");
		
		Lista lista2 = new Lista("1234", "Lenguajes");
		lista2.agregarItem("Java");
		lista2.agregarItem("Scala");
		
		// las listas se comparan por el nombre y el id
		assertEquals(lista1, lista2);
	}
	
	
	@Test
    public void testLimitarItems() {
    	Lista lista = new Lista("1234","Numeros");
    	
    	// se llena la lista
    	for(int i=1; i<=lista.getMaxItems(); i++) {
    		lista.agregarItem(String.valueOf(i));
    	}
    	
    	// agregar un item mas deberia lanzar excepcion
    	exception.expect(CustomForbiddenException.class);
    	lista.agregarItem("itemExtra");
    }
}
