package com.reception.projektkrivacic.model.classes;

import java.io.Serializable;

public class Address extends NamedEntity implements Serializable {

    private String street;
    private String city;
    private String country;
    private String postalCode;

    public Address(Long id, String name, String street, String city, String country, String postalCode) {
        super(id, name);
        this.street = street;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + country + " (" + postalCode + ")";
    }
}
