package com.tacs.deathlist.util;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import java.util.Map;


public class RequestUtils {


    public static String getTokenInCookies(HttpHeaders hh){
        Map<String, Cookie> pathParams = hh.getCookies();
        Cookie cookie = pathParams.get("token");
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }
}
