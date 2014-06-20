package com.tacs.deathlist.endpoint;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import com.tacs.deathlist.service.UserService;

@Path("/users/{uid}")
@Component
public class UsuarioEndpoints {

	@Autowired
	private UserService userService;

	/**
	 * Recupera un Usuario.
	 * 
	 * @param uid
	 * @return the http response
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("uid") String uid,
			@Context HttpHeaders hh) {
		String requestorToken = userService.getTokenInCookies(hh);

		// si la validación falla se lanza excepción
		userService.validateIdentity(requestorToken, uid);

		Usuario usuario = userService.getUsuarioFromUid(uid);

		return Response.status(Response.Status.OK).entity(usuario).build();
	}

	/**
	 * Loguea un usuario, creandolo si no existe.
	 * 
	 * @param uid
	 * @param hh
	 * @return la respuesta http.
	 */
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@PathParam("uid") String uid, @Context HttpHeaders hh) {
		String requestorToken = userService.getTokenInCookies(hh);

		userService.validateIdentity(requestorToken, uid);

		try {
			userService.createUsuarioFromToken(requestorToken);
		} catch (CustomForbiddenException e) {
			// ya existe, esta ok
		}

		return Response.status(Response.Status.OK).build();
	}

	/**
	 * Crea un nuevo Usuario.
	 * 
	 * @param uid
	 * @return the http response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(@PathParam("uid") String uid,
			@Context HttpHeaders hh) {
		String requestorToken = userService.getTokenInCookies(hh);

		userService.validateIdentity(requestorToken, uid);

		userService.createUsuarioFromToken(requestorToken);
		return Response.status(Response.Status.CREATED).build();
	}

	/**
	 * Elimina un usuario.
	 * 
	 * @param uid
	 * @return the http response
	 */
	@DELETE
	public Response deleteUser(@PathParam("uid") String uid,
			@Context HttpHeaders hh) {
		String requestorToken = userService.getTokenInCookies(hh);

		userService.validateIdentity(requestorToken, uid);

		userService.deleteUsuario(uid);

		return Response.status(Response.Status.OK).build();

	}

	/**
	 * Recupera las listas de los amigos de facebook del usuario.
	 * 
	 * @param uid
	 * @return the http response
	 */
	@Path("friends")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFriendsList(@PathParam("uid") String uid,
			@Context HttpHeaders hh) {
		String requestorToken = userService.getTokenInCookies(hh);

		userService.validateIdentity(requestorToken, uid);

		List<Usuario> friendsList = userService.getFriends(requestorToken);

		return Response.status(Response.Status.OK).entity(friendsList).build();
	}

}
