package me.gulsum.otopark.network;

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

