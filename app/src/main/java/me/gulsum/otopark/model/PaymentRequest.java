package me.gulsum.otopark.model;
import java.time.LocalDateTime;
public class PaymentRequest {
    private String cardNumber;
    private String cardExpiry;
    private String cardCVV;
    private String email;
    private String parkName;
    private double price;
    private String paymentId;
    private String reservationId;
    private long createdAt;


    public PaymentRequest(String email, String cardNumber, String cardExpiry, String cardCVV, String paymentId, String parkName, double price, long createdAt) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardCVV = cardCVV;
        this.email = email;
        this.parkName = parkName;
        this.price = price;
        this.createdAt = createdAt;
    }

    public String getPaymentId(){
        return paymentId;
    }

    public String getReservationId(){
        return reservationId;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public String getEmail() {
        return email;
    }
    public String getParkName() {
        return parkName;
    }

    public double getPrice() {
        return price;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public void setPaymentId(String paymentId){
        this.paymentId = paymentId;
    }

    public void setReservationId(String reservationId){
        this.reservationId= reservationId;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCreatedAt(long  createdAt) {
        this.createdAt = createdAt;
    }
}

