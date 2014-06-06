package com.tacs.deathlist.service;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.FacebookType;
import com.tacs.deathlist.PropertiesManager;

public class FacebookAppService {
	
	private static final PropertiesManager propertiesManager = new PropertiesManager();
	
	
	
	public void enviarNotificacion(String fbUserId, String mensaje) {
	    
	    AccessToken appAccessToken = new DefaultFacebookClient().obtainAppAccessToken(propertiesManager.getProperty("facebook.app.id"), propertiesManager.getProperty("facebook.app.secret"));
	    
	    FacebookClient facebookClient = new DefaultFacebookClient(appAccessToken.getAccessToken());
	    
	    try {
	        
	        facebookClient.publish(fbUserId + "/notifications", FacebookType.class, Parameter.with("template", mensaje));
	    }
	    
	    catch (FacebookOAuthException e) {
	        
	        if (e.getErrorCode() == 200) {
	            // El usuario receptor no usa Deathlist
	        } else if (e.getErrorCode() == 100) {
	            // El mensaje supera los 180 caracteres
	        }
	    }
	}

}
