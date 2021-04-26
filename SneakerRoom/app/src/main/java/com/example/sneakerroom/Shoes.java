package com.example.sneakerroom;

import java.io.Serializable;

public class Shoes implements Serializable {
    private int sneakerID;
    private String sneakerName;
    private String colorway;
    private double price;
    private String condition;
    private int userID;

    public Shoes(int sneakerID, String sneakerName, String colorway, double price, String condition, int userID) {
        this.sneakerID = sneakerID;
        this.sneakerName = sneakerName;
        this.colorway = colorway;
        this.price = price;
        this.condition = condition;
        this.userID = userID;
    }

    public int getSneakerID() {
        return sneakerID;
    }

    public void setSneakerID(int sneakerID) {
        this.sneakerID = sneakerID;
    }

    public String getSneakerName() {
        return sneakerName;
    }

    public void setSneakerName(String sneakerName) {
        this.sneakerName = sneakerName;
    }

    public String getColorway() {
        return colorway;
    }

    public void setColorway(String colorway) {
        this.colorway = colorway;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Shoes{" +
                "sneakerID=" + sneakerID +
                ", sneakerName='" + sneakerName + '\'' +
                ", colorway='" + colorway + '\'' +
                ", price=" + price +
                ", condition='" + condition + '\'' +
                ", userID=" + userID +
                '}';
    }
}
