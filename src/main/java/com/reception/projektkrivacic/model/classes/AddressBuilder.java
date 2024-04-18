package com.reception.projektkrivacic.model.classes;

public class AddressBuilder {
    private Long id;
    private String name;
    private String street;
    private String city;
    private String country;
    private String postalCode;

    public AddressBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public AddressBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public AddressBuilder setStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    public AddressBuilder setCountry(String country) {
        this.country = country;
        return this;
    }

    public AddressBuilder setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public Address createAddress() {
        return new Address(id, name, street, city, country, postalCode);
    }
}