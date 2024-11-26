package me.gulsum.otopark.model;
import me.gulsum.otopark.UI.Model.Payment;
import me.gulsum.otopark.UI.Model.User;

public class PaymentResponse {
    private String message;
    private Payment payment ;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(me.gulsum.otopark.UI.Model.Payment payment) {
        this.payment = payment;
    }
}





