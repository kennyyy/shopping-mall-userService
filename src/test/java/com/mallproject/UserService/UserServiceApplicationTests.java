package com.mallproject.UserService;

import com.mallproject.UserService.security.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {
	@Autowired
	private JWTService jwtService;

	@Test
	void contextLoads() {
		System.out.println(jwtService);
	}

}
