package com.tacs.deathlist.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Item implements Comparable<Item>{

    @Id
	private String nombre;
	private Integer votos;

    public Item(){}

	public Item(String nombre) {
		this.nombre = nombre;		
		this.votos = 0;
	}
	
	public String getNombre() {
        return nombre;
    }

    public Integer getVotos() {
		return this.votos;
	}
	
	public void recibirVoto() {		
		this.votos = this.votos + 1;
	}

	@Override
	public String toString() {
		return "Item [nombre=" + nombre + ", votos=" + votos + "]";
	}

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
            .append(nombre)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Item){
            final Item other = (Item) obj;
            return new EqualsBuilder()
                .append(nombre, other.getNombre())
                .isEquals();
        } else{
            return false;
        }
    }

    @Override
    public int compareTo(Item o) {
        return o.getVotos().compareTo(this.votos);
    }
}
