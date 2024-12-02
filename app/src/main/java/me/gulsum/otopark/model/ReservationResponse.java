package me.gulsum.otopark.model;

import me.gulsum.otopark.UI.Model.Reservation;

public class ReservationResponse {
    private String message;
    private Reservation data;

    // Constructor
    public ReservationResponse(String message, Reservation data) {
        this.message = message;
        this.data = data;
    }

    // Default Constructor
    public ReservationResponse() {
    }

    // Getter ve Setter metodları
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Reservation getData() {
        return data;
    }

    public void setData(Reservation data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReservationResponse{" +
                "message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
