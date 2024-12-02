package me.gulsum.otopark.network;

<<<<<<< HEAD
import java.util.List;

import me.gulsum.otopark.UI.Model.Payment;
import me.gulsum.otopark.UI.Model.Reservation;
import me.gulsum.otopark.UI.Model.User;
import me.gulsum.otopark.model.LoginRequest;
import me.gulsum.otopark.model.LoginResponse;
import me.gulsum.otopark.model.PaymentRequest;
import me.gulsum.otopark.model.PaymentResponse;
import me.gulsum.otopark.model.RegisterRequest;
import me.gulsum.otopark.model.RegisterResponse;
import me.gulsum.otopark.model.ReservationRequest;
import me.gulsum.otopark.model.ReservationResponse;
import me.gulsum.otopark.model.UserReservationPaymentResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Kullanıcı kaydı
    @POST("/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    // Kullanıcı giriş
    @POST("/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    // Email ile kullanıcı bilgisi alma
    @GET("/user/{email}")
    Call<User> getUserByEmail(@Path("email") String email);

    // Ödeme oluşturma
    @POST("/payment")
    Call<PaymentResponse> makePayment(@Body PaymentRequest paymentRequest);

    // Ödemeleri e-posta ile sorgulama
    @GET("/payments")
    Call<List<Payment>> getPaymentsByEmail(@Query("email") String email);

    // Rezervasyon ekleme
    @POST("/reservation")
    Call<ReservationResponse> saveReservationToDatabase(@Body ReservationRequest reservationRequest);

    // Email ile rezervasyon sorgulama
    @GET("/reservations")
    Call<List<Reservation>> getReservationsByEmail(@Query("email") String email);

    // Kullanıcı e-posta ile rezervasyon ve ödeme bilgilerini çekme
    @GET("/user/{email}/reservation-payment")
    Call<UserReservationPaymentResponse> getReservationAndPaymentInfo(@Path("email") String email);

    // Rezervasyon iptali
    @POST("/reservation/cancel")
    Call<ReservationResponse> cancelReservation(@Body ReservationRequest reservationRequest);


}
=======
import me.gulsum.otopark.model.LoginRequest;
import me.gulsum.otopark.model.LoginResponse;
import me.gulsum.otopark.model.RegisterRequest;
import me.gulsum.otopark.model.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

        @POST("/login")
        Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
    }

>>>>>>> 9cf18ff20f6e53bc8bfe893cac2c31d39e010fb5
