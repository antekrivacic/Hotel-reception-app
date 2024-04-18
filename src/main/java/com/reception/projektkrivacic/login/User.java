package com.reception.projektkrivacic.login;

import com.reception.projektkrivacic.model.classes.Address;
import com.reception.projektkrivacic.model.classes.NamedEntity;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

public class User extends NamedEntity implements Serializable {
    private static final long serialVersionUID = 8025398221329501493L;

    private String password;
    private String firstName;
    private String lastName;
    private Boolean isAnEmployee;
    private Address address;

    public User(Long id, String username, String firstName, String lastName, Boolean isAnEmployee,
                Address address) {  //general constructor
        super(id, username);
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAnEmployee = isAnEmployee;
        this.address = address;
    }

    public User(String username, String password) { // Constructor for hashed password file
        super(null, username);
        this.password = password;
    }
    public User() {
        super();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getAnEmployee() {
        return isAnEmployee;
    }

    public void setAnEmployee(Boolean anEmployee) {
        isAnEmployee = anEmployee;
    }

    public String getPassword() { return password; }

    public String getHashedPassword() {
        return hashPassword(password);
    }

    public Boolean getIsAnEmployee() { return isAnEmployee; }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getRoleToString() {
        return isAnEmployee ? "Employee" : "Guest";
    }

    @Override
    public String toString() {
        return  "FirstName='" + firstName + '\'' +
                ", LastName='" + lastName + '\'' +
                ", IsAnEmployee=" + isAnEmployee +
                ", Address=" + address.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(isAnEmployee, user.isAnEmployee) && Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, firstName, lastName, isAnEmployee, address);
    }
}
