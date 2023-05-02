package com.sbu.dj;

import com.sbu.dj.domain.user.User;
import com.sbu.dj.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Autowired
	private Environment environment;

	@Value("${server.port}")
	private int serverPort;

	@Value("${server.servlet.context-path}")
	private String baseUrl;

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		String hostAddress = "";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ignored) {

		}

		String mainUrl = "http://" + hostAddress + ":" + serverPort + baseUrl;
		System.out.println("SwaggerUI URL: " + mainUrl + "/swagger-ui/index.html");
	}

	@Bean
	AuditorAware<User> auditorProvider(UserService userService) {
		return () -> Optional.ofNullable(userService.getCurrentUserNN());
	}
}
