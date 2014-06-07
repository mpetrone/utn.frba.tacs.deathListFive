package com.tacs.deathlist.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.FacebookType;

@Component
public class FacebookAppService implements AppService {
	
	private String appId;
    private String appSecret;
    
    @Value("${facebook.app.id}")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Value("${facebook.app.secret}")
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
	
	@Override
    public void enviarNotificacion(String uidReceptor, String mensaje) {
	    
	    AccessToken appAccessToken = new DefaultFacebookClient().obtainAppAccessToken(this.appId, this.appSecret);	    
	    FacebookClient facebookClient = new DefaultFacebookClient(appAccessToken.getAccessToken());
	    
	    try {	        
	        facebookClient.publish(uidReceptor + "/notifications", FacebookType.class, Parameter.with("template", mensaje));
	    }	    
	    catch (FacebookOAuthException e) {
	        
	        if (e.getErrorCode() == 200) {
	            // El usuario receptor no usa Deathlist
	        } else if (e.getErrorCode() == 100) {
	            // El mensaje supera los 180 caracteres
	        }
	    }
	}
	
	@Override
	public void publicarEnNewsfeed(String uid, String mensaje) {
		
		// TODO: Decidir si esto se hace en el frontend (botón compartir)
	}

}
