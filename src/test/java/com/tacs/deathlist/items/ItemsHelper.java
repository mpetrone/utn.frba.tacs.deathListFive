package com.tacs.deathlist.items;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tacs.deathlist.PropertiesManager;

public class ItemsHelper {
    
    private WebTarget target;
    
    public ItemsHelper(PropertiesManager propertiesManager) {
        Client c = ClientBuilder.newClient();
        target = c.target(propertiesManager.getProperty("base.uri"));
    }

    public Response createItem(String username, String listaName, String itemName) {          
        return target.path("/users/" + username + "/lists/" + listaName + "/items/" + itemName)
                .request(MediaType.APPLICATION_JSON).post(null);
                
    }   

    public Response deleteItem(String username, String listaName, String itemName) {
        return target.path("/users/" + username + "/lists/" + listaName + "/items/" + itemName)
                .request(MediaType.APPLICATION_JSON).delete();
    }

    public Response voteItem(String username, String listaName, String itemName) {
        return target.path("/users/" + username + "/lists/" + listaName + "/items/" + itemName + "/vote")
                .request(MediaType.APPLICATION_JSON).post(null);
    }

}
