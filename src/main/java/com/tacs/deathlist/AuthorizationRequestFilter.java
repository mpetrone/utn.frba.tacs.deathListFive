package com.tacs.deathlist;

import com.tacs.deathlist.Service.UserService;
import com.tacs.deathlist.domain.Usuario;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {
        Cookie tokenCookie = requestContext.getCookies().get("token");
        if (tokenCookie == null || StringUtils.isEmpty(tokenCookie.getValue())) {
            requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).
                    entity("No se encontro el token").build());
        }

        Usuario user = userService.getUser(tokenCookie.getValue());

        if (user == null) {
            requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).
                    entity("El token es invalido").build());
        }
    }
}
