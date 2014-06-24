package com.tacs.deathlist.usuario;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.tacs.deathlist.DeathListTest;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import com.tacs.deathlist.service.UserService;

public class UserServiceTest extends DeathListTest {
	
	private final UserService userService = new StubUserService();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testAmistad() {
		
		String token1 = "user1";
		
		String uid2 = "2";
		String uid3 = "3";
		
		// 1 y 3 son amigos, deberia dar ok
		this.userService.validateIdentityOrFriendship(token1, uid3);
		
		// 1 y 2 no son amigos, deberia lanzar excepcion
		exception.expect(CustomForbiddenException.class);
		this.userService.validateIdentityOrFriendship(token1, uid2);
	}

}
