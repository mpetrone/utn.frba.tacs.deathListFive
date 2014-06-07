package com.tacs.deathlist.service;

public interface AppService {
	
	public void enviarNotificacion(String uidReceptor, String mensaje);
	
	public void publicarEnNewsfeed(String uid, String mensaje);

}
