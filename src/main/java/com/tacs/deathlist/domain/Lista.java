package com.tacs.deathlist.domain;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Unindex;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Cache
public class Lista {

    @Id
    private String id;
    private String nombre;
    @Unindex
    private List<Item> items = new ArrayList<Item>();

    //TODO: ponerlo en las properties
    private static int MAX_ITEMS = 10;

    public Lista(){}
    
    public Lista(String id, String nombre) {
        this.nombre = nombre;
        this.id = id;
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
    
    public int getMaxItems() {
    	return MAX_ITEMS;
    }

    /**
     * Este método agrega un nuevo item a la lista.
     * 
     * @param nombreDeNuevoItem el nombre del item
     * 
     * @throws CustomForbiddenException si el item excede la capacidad máxima
     * de la lista o si un item con ese nombre ya existe en la lista
     * 
     */	
    public void agregarItem(String nombreDeNuevoItem) {
		if (!existeItem(nombreDeNuevoItem)) {
			if (this.getCantidadDeItems() < MAX_ITEMS)
				items.add(new Item(nombreDeNuevoItem));
			else
				throw new CustomForbiddenException("La lista " + this.getNombre()
						+ " ya tiene el número máximo permitido de items ("
						+ MAX_ITEMS + ").");
		} else
			throw new CustomForbiddenException("El item " + nombreDeNuevoItem
					+ " ya existe en la lista " + this.getNombre() + ".");
	}
    
	/**
     * Este método obtiene un item de la lista.
     * 
     * @param itemName el nombre del item
     * @return el item buscado
     * @throws CustomNotFoundException si el item no existe en la lista
     * 
     */	
	public Item getItem(String itemName) {
		for (Item item : items) {
			if (itemName.equals(item.getNombre())) {
				return item;
			}
		}
		throw new CustomNotFoundException("El item " + itemName + " no existe en la lista " + this.getNombre() + ".");
	}
    
	/**
     * Este método elimina un item de la lista.
     * 
     * @param itemName el nombre del item
     * 
     * @throws CustomNotFoundException si el item no existe en la lista
     * 
     */	
	public void eliminarItem(String itemName){
        if(!items.remove(new Item(itemName))) {
        	throw new CustomNotFoundException("El item " + itemName + " no existe en la lista " + this.getNombre() + ".");
        }
    }
    
	/**
     * Este método suma un voto a un item de la lista.
     * 
     * @param itemName el nombre del item
     * 
     * @throws CustomNotFoundException si el item no existe en la lista
     * 
     */	
	public void votarItem(String itemName) {
        getItem(itemName).recibirVoto();
    }

    @Override
    public String toString() {
        return "Lista [nombre=" + nombre + ", items=" + items + "]";
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
        .append(nombre)
        .append(id)
        .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Lista){
            final Lista other = (Lista) obj;
            return new EqualsBuilder()
            .append(id, other.id)
            .append(nombre, other.getNombre())
            .isEquals();
        } else{
            return false;
        }
    }
}
