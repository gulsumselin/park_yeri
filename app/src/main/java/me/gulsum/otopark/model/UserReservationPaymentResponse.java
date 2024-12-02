package me.gulsum.otopark.model;

import java.util.List;

import me.gulsum.otopark.UI.Model.Payment;
import me.gulsum.otopark.UI.Model.Reservation;
import me.gulsum.otopark.UI.Model.User;

public class UserReservationPaymentResponse {
    private String message;
    private User user;
    private List<Reservation> reservations;
    private List<Payment> payments;

    // Getter ve Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}

