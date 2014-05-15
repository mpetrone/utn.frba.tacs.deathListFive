package com.tacs.deathlist.endpoints.resources;


/**
 * 
 * Representacion del request en el create User.
 * 
 * @author matias.petrone
 *
 */
public class UserCreationRequest {

    private String uid;
    private String token;
    
    // lo pide GSON
    UserCreationRequest(){}; 
    
    public UserCreationRequest(String uid, String token) {
        super();
        this.uid = uid;
        this.token = token;
    }
    public String getUid() {
        return uid;
    }
    public String getToken() {
        return token;
    }
}
