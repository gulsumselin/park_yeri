package me.gulsum.otopark.UI.Model;

import java.time.LocalDateTime;

public class Payment {

    private String paymentId;       // Unique ID for the payment
    private String email;           // User email for payment
    private String status;          // Payment status (e.g., "Pending", "Completed")
    private String parkName;        // Name of the parking area
    private LocalDateTime createdAt; // Payment time (to be set externally)
    private double price;           // Payment price
    public Payment(String paymentId, String email, String status, String parkName, LocalDateTime createdAt, double price) {
        this.paymentId = paymentId;
        this.email = email;
        this.status = status;
        this.parkName = parkName;
        this.createdAt = createdAt;
        this.price = price;
    }

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
