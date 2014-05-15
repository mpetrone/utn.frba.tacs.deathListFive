package com.tacs.deathlist.domain;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class CustomForbiddenException extends WebApplicationException {
	
    private static final long serialVersionUID = 13754947979705084L;

	  /**
	  * Create a HTTP 403 (Forbidden) exception.
	  * @param message the String that is the entity of the 403 response.
	  */
	public CustomForbiddenException(String message) {
		super(Response.status(Response.Status.FORBIDDEN).entity(message)
				.type("text/plain").build());
	}



}
