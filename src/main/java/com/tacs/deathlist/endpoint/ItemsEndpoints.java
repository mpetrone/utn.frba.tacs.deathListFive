package com.tacs.deathlist.endpoint;

import com.tacs.deathlist.dao.ListasDao;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.service.UserService;
import com.tacs.deathlist.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/users/{uid}/lists/{listName}/items/{itemName}")
@Component
public class ItemsEndpoints {

    @Autowired
    private ListasDao dao;
    
    @Autowired
    private UserService userService;
	
    /**
	 * Crea un nuevo ítem en una lista.
	 * 
	 * @param uid
	 * @param listName
	 * @param itemName
	 * @return
	 */
	@POST
	public Response createItem(@PathParam("uid") String uid,
			                   @PathParam("listName") String listName,
			                   @PathParam("itemName") String itemName,
			                   @Context HttpHeaders hh) {
        String requestorToken = RequestUtils.getTokenInCookies(hh);
         
        Usuario usuario = userService.getUsuarioRequestor(requestorToken, uid);

        if(usuario == null){
            return Response.status(Status.NOT_FOUND).entity("el usuario no existe").build();
        }

		dao.createItem(uid, listName, itemName);

        if(!usuario.getUid().equalsIgnoreCase(uid)){
            userService.enviarNotificacion(uid,
                    usuario.getNombre() + " ha creado el item " + itemName + " en la lista " + listName);
        }
        
		return Response.status(Status.CREATED).build();
	}

	/**
	 * Votación de un ítem.
	 * 
	 * @param itemName
	 * @return
	 */
	@Path("vote")
	@POST
	public Response voteItem(@PathParam("uid") String uid,
			                 @PathParam("listName") String listName,
			                 @PathParam("itemName") String itemName,
			                 @Context HttpHeaders hh) {
        String requestorToken = RequestUtils.getTokenInCookies(hh);

        Usuario usuario = userService.getUsuario(requestorToken, uid);

        if (usuario == null) {
            return Response.status(Status.NOT_FOUND).entity("el usuario no existe").build();
        }

        userService.publicarEnNewsfeed(requestorToken, "Voté el item " + itemName + "en la lista " + listName);

        dao.voteItem(uid, listName, itemName);

        return Response.status(Status.CREATED).build();
    }

	/**
	 * Elimina un item.
	 * 
	 * @param itemName
	 * @return
	 */
	@DELETE
	public Response deleteItem(@PathParam("uid") String uid,
			                   @PathParam("listName") String listName,
			                   @PathParam("itemName") String itemName,
			                   @Context HttpHeaders hh) {
        String requestorToken = RequestUtils.getTokenInCookies(hh);

        Usuario usuario = userService.getUsuario(requestorToken, uid);
        Usuario usuarioRequestor = userService.getUsuarioRequestor(requestorToken, uid);

        if (usuario == null) {
            return Response.status(Status.NOT_FOUND).entity("el usuario no existe").build();
        }
        
        if(!usuarioRequestor.getUid().equalsIgnoreCase(uid)) {
            return Response.status(Status.FORBIDDEN).entity("No se puede eliminar items en listas de otros usuarios").build();
        }

        dao.deleteItem(uid, listName, itemName);

        return Response.status(Status.OK).build();
	}


}
