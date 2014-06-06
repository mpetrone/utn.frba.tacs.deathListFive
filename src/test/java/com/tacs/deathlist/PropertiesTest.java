package com.tacs.deathlist;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PropertiesTest {
	
	@Test
	public void testObtenerPropiedades() {
		
		PropertiesManager pm = new PropertiesManager();
		
		String facebookAppId = pm.getProperty("facebook.app.id");
		String facebookAppSecret = pm.getProperty("facebook.app.secret");
		
		assertEquals("1507322809490755", facebookAppId);
		assertEquals("87fc8b8d9bf581b16f8c85d61a4e7a8b", facebookAppSecret);
		
	}

}
