package me.gulsum.otopark.UI.Model;

import java.io.Serializable;

public class Reservation implements Serializable {
    private String parkName;

    private String parkingSpot;
    private String startTime;

    private String endTime;
    private double price;

    // Constructor
    public Reservation(String parkName, String startTime, double price) {
        this.parkName = parkName;
        this.parkingSpot = parkingSpot;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    // Getters and Setters
    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getParkingSpot() {return parkingSpot;}

    public void setParkingSpot(String parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getEndTime() { return endTime;}
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

