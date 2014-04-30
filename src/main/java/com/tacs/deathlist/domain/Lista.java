package com.tacs.deathlist.domain;

import java.util.Collections;
import java.util.LinkedList;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Lista {

    private String nombre;
    private LinkedList<Item> items;

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

    public void agregarItem(String nombreDeNuevoItem) {
        if(!items.contains(new Item(nombreDeNuevoItem))){
            items.add(new Item(nombreDeNuevoItem));
        }
    }
    
    public Item getItem(String itemName) {
        for (Item item : items) {
            if(itemName.equals(item.getNombre())){
                return item;
            }
        }
        return null;
    }   
    
    public void eliminarItem(String itemName){
        items.remove(new Item(itemName));
    }
    
    public void votarItem(String itemName) {
        if(existeItem(itemName)){
            getItem(itemName).recibirVoto();
            Collections.sort(items);
        }
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
