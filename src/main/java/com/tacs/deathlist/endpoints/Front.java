package com.tacs.deathlist.endpoints;

import java.io.StringWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

@Path("/front")
public class Front {
    
    String front;
    
    {
        // inicializacion del engine velocity, cuando tengamos el proyecto bien
        // armado esto va a estar en algun otro lado
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, 
                "./src/main/resources/com/tacs/deathlist/templates/");
        ve.init();
        Template t = ve.getTemplate("front.vm");
        VelocityContext context = new VelocityContext();
        context.put( "item1", new String("DeathList1") );
        context.put( "item2", new String("DeathList2") );
        context.put( "item3", new String("DeathList3") );
        StringWriter sw = new StringWriter();
        t.merge( context, sw );
        front = sw.toString();
    }
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getFront() { 
        return Response.status(Response.Status.OK).entity(front).build();
    } 

}
