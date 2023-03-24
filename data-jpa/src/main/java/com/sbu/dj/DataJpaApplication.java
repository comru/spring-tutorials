package com.sbu.dj;

import com.sbu.dj.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	AuditorAware<User> auditorAware() {
		return new UserAuditorAware();
	}

	public static class UserAuditorAware implements AuditorAware<User> {
		@Override
		public Optional<User> getCurrentAuditor() {
			return Optional.of(new User());
		}
	}
}
