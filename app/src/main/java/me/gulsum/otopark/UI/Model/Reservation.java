package me.gulsum.otopark.UI.Model;

import java.util.Date;

public class Reservation {
    private String id;
    private String email;
    private String parkName;
    private String reservationId;
    private String entryTime;
    private double payment;
    private Date createdAt;
    private Date updatedAt;
    private int __v;

    // Constructor
    public Reservation(String id, String email, String parkName, String reservationId, String entryTime,
                       double payment, Date createdAt, Date updatedAt, int __v) {
        this.id = id;
        this.email = email;
        this.parkName = parkName;
        this.reservationId = reservationId;
        this.entryTime = entryTime;
        this.payment = payment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.__v = __v;
    }

    // Default Constructor
    public Reservation() {
    }

    // Getter ve Setter metodları
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }


    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", parkName='" + parkName + '\'' +
                ", reservationId='" + reservationId + '\'' +
                ", entryTime='" + entryTime + '\'' +
                ", payment=" + payment +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", __v=" + __v +
                '}';
    }
}
