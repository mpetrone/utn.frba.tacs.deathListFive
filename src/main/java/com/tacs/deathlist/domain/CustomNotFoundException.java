package com.tacs.deathlist.domain;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class CustomNotFoundException extends WebApplicationException {
	
    private static final long serialVersionUID = -4741874630269920223L;

    /**
	  * Create a HTTP 404 (Not Found) exception.
	  */
	  public CustomNotFoundException() {
	    super(Response.status(Response.Status.NOT_FOUND).build());
	  }

	  /**
	  * Create a HTTP 404 (Not Found) exception.
	  * @param message the String that is the entity of the 404 response.
	  */
	public CustomNotFoundException(String message) {
		super(Response.status(Response.Status.NOT_FOUND).entity(message)
				.type("text/plain").build());
	}

}
