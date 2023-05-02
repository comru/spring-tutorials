package com.sbu.dj;

import com.sbu.dj.domain.user.User;
import com.sbu.dj.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DataJpaApplicationTests {
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		User user = new User()
				.setEmail("ivanov@email.com")
				.setUsername("Ivan")
				.setPasswordHash("abc");
		userRepository.save(user);
	}

	@Test
	void findAll() {
		List<User> allUsers = userRepository.findAll();
		String username = allUsers.get(0).getUsername();
		Assertions.assertEquals("Ivan", username);
	}

}
