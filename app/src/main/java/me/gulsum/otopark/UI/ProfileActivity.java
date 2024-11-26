package me.gulsum.otopark.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.gulsum.otopark.R;
import me.gulsum.otopark.UI.Model.User;
import me.gulsum.otopark.UI.Screen.OnBoard.ReservationDetailsActivity;
import me.gulsum.otopark.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewUsername, textViewEmail, textViewPhone, textViewCarPlate, textViewCarType;
    private Button buttonLogout, konumaGitButton, buttonReservation;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // View'leri tanımalar
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewCarPlate = findViewById(R.id.textViewCarPlate);
        textViewCarType = findViewById(R.id.textViewCarType);
        buttonLogout = findViewById(R.id.buttonLogout);
        konumaGitButton = findViewById(R.id.konuma_git_button);
        buttonReservation = findViewById(R.id.buttonReservation);

        // Kayıtlı kullanıcı e-posta bilgisini al
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("user_email");

        if (userEmail != null) {
            loadUserProfile(userEmail); // Kullanıcı profilini yükle
        } else {
            Toast.makeText(this, "Kullanıcı bilgileri bulunamadı.", Toast.LENGTH_SHORT).show();
        }

        // Çıkış yap butonuna tıklama olayı
        buttonLogout.setOnClickListener(v -> {
            sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            Toast.makeText(ProfileActivity.this, "Çıkış yapıldı.", Toast.LENGTH_SHORT).show();
            finish(); // Aktiviteyi kapat
        });

        // Otopark ara butonuna tıklama olayı
        konumaGitButton.setOnClickListener(v -> {
            Intent parkSelectionIntent = new Intent(ProfileActivity.this, UserMapsActivity.class);
            startActivity(parkSelectionIntent);
        });

        buttonReservation.setOnClickListener(v -> {
            Intent reservationIntent = new Intent(ProfileActivity.this, ReservationDetailsActivity.class);
            // Kullanıcı bilgilerini Intent'e ekle
            reservationIntent.putExtra("user_email", textViewEmail.getText().toString());
            reservationIntent.putExtra("user_name", textViewUsername.getText().toString());
            reservationIntent.putExtra("user_phone", textViewPhone.getText().toString());
            reservationIntent.putExtra("user_car_plate", textViewCarPlate.getText().toString());
            reservationIntent.putExtra("user_car_type", textViewCarType.getText().toString());
            startActivity(reservationIntent);
        });



    }



    // Retrofit ile profil verilerini al
    private Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/") // Backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void loadUserProfile(String email) {
        Retrofit retrofit = getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);

        // Kullanıcı bilgilerini alma isteği
        Call<User> call = apiService.getUserByEmail(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    // TextView'leri doldur
                    textViewUsername.setText("Kullanıcı Adı: " + user.getName());
                    textViewEmail.setText("E-posta: " + user.getEmail());
                    textViewPhone.setText("Telefon Numarası: " + user.getPhone());
                    textViewCarPlate.setText("Araç Plakası: " + user.getCarPlate());
                    textViewCarType.setText("Araç Tipi: " + user.getCarType());
                } else {
                    Toast.makeText(ProfileActivity.this, "Kullanıcı bilgileri yüklenemedi.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Hata: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
