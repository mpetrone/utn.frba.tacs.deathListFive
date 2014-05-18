package com.tacs.deathlist.endpoints.resources;


/**
 * 
 * Representacion del request en el create User.
 * 
 * @author matias.petrone
 *
 */
public class UserCreationRequest {

    private String nombre;
    
    // lo pide GSON
    UserCreationRequest(){}; 
    
    public UserCreationRequest(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
