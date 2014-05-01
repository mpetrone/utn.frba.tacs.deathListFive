package com.tacs.deathlist.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Lista {

    private String nombre;
    private List<Item> items;
    
    // Todas las listas están limitadas a tener una cierta cantidad de items
    private static final int MAX_ITEMS = 10;

    public Lista(String nombre) {
        this.nombre = nombre;
        this.items = new LinkedList<Item>();
    }

    public String getNombre() {
        return this.nombre;
    }

    public boolean existeItem(String itemName) {
        return this.items.contains(new Item(itemName));
    }
    
    private int getCantidadDeItems() {
    	return this.items.size();
    }

	public void agregarItem(String nombreDeNuevoItem) {
		if (!existeItem(nombreDeNuevoItem)) {
			if (this.getCantidadDeItems() < MAX_ITEMS)
				items.add(new Item(nombreDeNuevoItem));
			// logueamos la insercion de items?
			else
				throw new LimiteDeItemsException("La lista " + this.getNombre()
						+ " ya tiene el número máximo permitido de items ("
						+ MAX_ITEMS + ").");
		} else
			throw new ItemRepetidoException("El item " + nombreDeNuevoItem
					+ " ya existe en la lista " + this.getNombre() + ".");
	}
    
	public Item getItem(String itemName) {
		for (Item item : items) {
			if (itemName.equals(item.getNombre())) {
				return item;
			}
		}
		throw new ItemInexistenteException("El item " + itemName + " no existe en la lista " + this.getNombre() + ".");
	}
    
    public void eliminarItem(String itemName){
        if(items.remove(new Item(itemName))) {
        	// borrado exitoso. (lo registramos con el logger?)
        }
        else {
        	throw new ItemInexistenteException("El item " + itemName + " no existe en la lista " + this.getNombre() + ".");
        }
    }
    
    public void votarItem(String itemName) {
        getItem(itemName).recibirVoto(); // lanza excepcion si el item no existe
        Collections.sort(items);
    }

    @Override
    public String toString() {
        return "ListaInMemory [nombre=" + nombre + ", items=" + items + "]";
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
        .append(nombre)
        .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Lista){
            final Lista other = (Lista) obj;
            return new EqualsBuilder()
            .append(nombre, other.getNombre())
            .isEquals();
        } else{
            return false;
        }
    }
}
