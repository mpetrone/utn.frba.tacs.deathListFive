package com.tacs.deathlist;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

@Path("lists")
public class Listas {
    
    private List<String> list = new ArrayList<>();
    private Gson gsonParser = new Gson();
    
    public Listas() {
        list.add("elemento 1");
        list.add("elemento 2");
        list.add("elemento 3");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getLists() {
        return gsonParser.toJson(list);
    }
}
