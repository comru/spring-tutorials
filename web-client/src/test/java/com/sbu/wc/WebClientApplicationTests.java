package com.sbu.wc;

import com.sbu.wc.model.User;
import com.sbu.wc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WebClientApplicationTests {
	@Autowired
	private UserService userService;

	@Test
	void getUsers() {
		List<User> users = userService.getUsers();
		assertEquals(10, users.size());
		for (User user : users) {
			assertNotNull(user.name());
		}
	}

}
