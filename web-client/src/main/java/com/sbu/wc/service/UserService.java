package com.sbu.wc.service;


import com.sbu.wc.model.User;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange("/users")
public interface UserService {
    @GetExchange
    List<User> getUsers();

}
