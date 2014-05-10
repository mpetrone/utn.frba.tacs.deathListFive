package com.tacs.deathlist.endpoints.resources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Representacion del request en el create User.
 * 
 * @author matias.petrone
 *
 */
@XmlRootElement
public class UserCreationRequest {

    private String uid;
    private String token;
    
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
