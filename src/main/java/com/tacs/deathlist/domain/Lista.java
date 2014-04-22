package com.tacs.deathlist.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lista {

    private String nombre;
    private List<Item> items;

    public Lista(String nombre) {
        this.nombre = nombre;
        this.items = new ArrayList<Item>();
    }

    public String getNombre() {
        return this.nombre;
    }

    public int size() {
        return this.items.size();
    }

    public boolean existeElItem(Item item) {
        return this.items.contains(item);
    }

    public int posicionDelItem(Item item) {
        return this.items.indexOf(item);
    }

    private int getVotosDelItemEnLaPosicion(int posicion) {
        return this.items.get(posicion).getVotos();
    }

    public void agregarItem(String nombreDeNuevoItem) {
        Item nuevoItem = new Item(nombreDeNuevoItem);
        this.items.add(nuevoItem);
    }

    /**
     * Este metodo registra el voto de un item de la lista
     * y asegura que permanezca ordenada por cantidad de votos.
     * 
     * @param nombreDelItem El nombre del item votado.
     */
    public void votarItem(String nombreDelItem) {

        int posicionDelItemVotado = this.posicionDelItem(new Item(nombreDelItem));
        Item itemVotado;
        int posicionNueva;

        if (posicionDelItemVotado != -1) {

            itemVotado = this.items.get(posicionDelItemVotado);
            itemVotado.recibirVoto();

            // si el voto fue para el primer item del ranking, no hay que intercambiar posiciones.
            if (posicionDelItemVotado > 0) {

                posicionNueva = posicionDelItemVotado;
                // la posicionNueva del item avanza tanto como corresponda, puede no avanzar nada
                while (posicionNueva >= 1 && this.getVotosDelItemEnLaPosicion(posicionDelItemVotado) > this.getVotosDelItemEnLaPosicion(posicionNueva - 1)) {
                    posicionNueva--;
                }

                Collections.swap(this.items, posicionNueva, posicionDelItemVotado);
            }


        }
        else {
            //TODO: lanzar excepcion? el item votado no existe
        }
    }
    
    @Override
    public String toString() {
        return "ListaInMemory [nombre=" + nombre + ", items=" + items + "]";
    }

    public boolean existeElItemConNombre(String nombre) {
        return items.contains(new Item(nombre));
    }

	public int getVotosDelItem(String itemName) {
		// Devuelve la cantidad de votos de un item		
		return this.getVotosDelItemEnLaPosicion(this.posicionDelItem(new Item(itemName)));
	}	


}
