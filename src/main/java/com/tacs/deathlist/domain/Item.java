package com.tacs.deathlist.domain;

public class Item {
	
	private String nombre;
	private int votos;
	
	public Item(String nombre) {
		this.nombre = nombre;		
		this.votos = 0;
	}
	
	public String getNombre() {
        return nombre;
    }

    public int getVotos() {
		return this.votos;
	}
	
	public void recibirVoto() {		
		this.votos = this.votos + 1;
	}

	@Override
	public String toString() {
		return "ItemInMemory [nombre=" + nombre + ", votos=" + votos + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	
	

}
