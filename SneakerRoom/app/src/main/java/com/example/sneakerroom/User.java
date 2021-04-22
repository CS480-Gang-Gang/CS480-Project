package com.example.sneakerroom;

public class User {
    private int idUser;
    private String firstName;
    private String lastName;
    private int numShoes;
    private String address;
    private String email;
    private String password;
    private String passwordRep;

    public User(int idUser, String firstName, String lastName, int numShoes, String address, String email, String password, String passwordrep) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.numShoes = numShoes;
        this.address = address;
        this.email = email;
        this.password = password;
        this.passwordRep = passwordRep;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

    public int getNumShoes() {
        return numShoes;
    }

    public void setNumShoes(int numShoes) {
        this.numShoes = numShoes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRep() {
        return passwordRep;
    }

    public void setPasswordRep(String passwordRep) {
        this.passwordRep = passwordRep;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", numShoes=" + numShoes +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwordRep='" + passwordRep + '\'' +
                '}';
    }
}
