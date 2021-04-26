package com.example.sneakerroom;

public class User {
    private int idUser;
    private String firstName;
    private String lastName;
    private int numShoes;
    private String address;
    private String city;
    private String state;
    private String phoneNum;
    private String userName;
    private String password;
    private int zip;

    public User(int idUser, String firstName, String lastName, int numShoes, String address, String userName, String password) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.numShoes = numShoes;
        this.address = address;
        this.userName = userName;
        this.password = password;
    }

    public User(int idUser, String firstName, String lastName, int numShoes, String address,
                String userName, String password, String city, String state, String phoneNum, int zip) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.numShoes = numShoes;
        this.address = address;
        this.userName = userName;
        this.password = password;
        this.city = city;
        this.state = state;
        this.phoneNum = phoneNum;
        this.zip = zip;
    }
    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    public String getUserName() {
        return userName;
    }

    public void setEmail(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", numShoes=" + numShoes +
                ", address='" + address + '\'' +
                ", email='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
