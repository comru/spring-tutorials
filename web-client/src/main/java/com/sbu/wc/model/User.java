package com.sbu.wc.model;

public record User(
        Integer id,
        String name,
        String username,
        String email,
        String phone,
        String website,
        Address address) {

}
