package me.gulsum.otopark.model;

public class ReservationRequest {
    private String email;
    private String parkName;
    private String reservationId;
    private String entryTime;
    private double payment;

    // Constructor
    public ReservationRequest(String email, String parkName, String reservationId, String entryTime, double payment) {
        this.email = email;
        this.parkName = parkName;
        this.reservationId = reservationId;
        this.entryTime = entryTime;
        this.payment = payment;
    }

    // Default Constructor
    public ReservationRequest() {
    }

    // Getter ve setter'lar
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

    @Override
    public String toString() {
        return "ReservationRequest{" +
                "email='" + email + '\'' +
                ", parkName='" + parkName + '\'' +
                ", reservationId='" + reservationId + '\'' +
                ", entryTime='" + entryTime + '\'' +
                ", payment=" + payment +
                '}';
    }
}
