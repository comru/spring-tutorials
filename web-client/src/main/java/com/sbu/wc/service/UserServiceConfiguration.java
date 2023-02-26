package com.sbu.wc.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class UserServiceConfiguration {
    @Bean
    public UserService userService() {
        WebClient client = WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build();

        HttpServiceProxyFactory proxyFactory =
                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();

        return proxyFactory.createClient(UserService.class);
    }
}
