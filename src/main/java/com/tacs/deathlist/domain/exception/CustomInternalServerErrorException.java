package com.tacs.deathlist.domain.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


public class CustomInternalServerErrorException extends WebApplicationException {


    /**
     * Create a HTTP 500 (Internal Server Error) exception.
     *
     * @param message the String that is the entity of the 500 response.
     */
    public CustomInternalServerErrorException(String message) {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message)
                .type("text/plain").build());
    }
}