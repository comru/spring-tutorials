package com.sbu.wc.model;

public record Address(
        String street,
        String suite,
        String city,
        String zipcode,
        Geo geo) {

    public record Geo(Double lat, Double lng) {

    }
}
