package com.tacs.deathlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaInMemory {
	
	private String nombre;
	private List<ItemInMemory> items;
	
	public ListaInMemory(String nombre) {
		this.nombre = nombre;
		this.items = new ArrayList<ItemInMemory>();
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public List<ItemInMemory> getRankingDeItems() {
		return this.items;
	}
	
	public int size() {
		return this.items.size();
	}
	
	public boolean existeElItem(ItemInMemory item) {
		return this.items.contains(item);
	}
	
	public boolean existeElItemConNombre(String nombre) {
		return this.existeElItem(new ItemInMemory(nombre));
	}
	
	public int posicionDelItem(ItemInMemory item) {
		return this.items.indexOf(item);
	}
	
	private int getVotosDelItemEnLaPosicion(int posicion) {
		return this.items.get(posicion).getVotos();
	}
	
	public void agregarItem(String nombreDeNuevoItem) {
		ItemInMemory nuevoItem = new ItemInMemory(nombreDeNuevoItem);
		
		if (!this.existeElItem(nuevoItem) )
			this.items.add(nuevoItem);
		
		else {
			//TODO: lanzar excepcion? el item a agregar ya existe
		}
		
	}
	
	/**
	 * Este metodo registra el voto de un item de la lista
	 * y asegura que permanezca ordenada por cantidad de votos.
	 * 
	 * @param nombreDelItem El nombre del item votado.
	 */
	public void votarItem(String nombreDelItem) {
		
		int posicionDelItemVotado = this.posicionDelItem(new ItemInMemory(nombreDelItem));
		ItemInMemory itemVotado;
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
	

}
