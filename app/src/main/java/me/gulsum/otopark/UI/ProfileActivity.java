package me.gulsum.otopark.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import me.gulsum.otopark.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewUsername, textViewEmail, textViewPhone, textViewCarPlate, textViewCarType;
    private Button buttonChangePassword, buttonUpdateProfile, buttonLogout, konuma_git_butonu;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewCarPlate = findViewById(R.id.textViewCarPlate);
        textViewCarType = findViewById(R.id.textViewCarType);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);
        buttonLogout = findViewById(R.id.buttonLogout);
        konuma_git_butonu = findViewById(R.id.konuma_git_button);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("user_email");
        if (userEmail != null) {
            loadUserProfile(userEmail);
        } else {
            Toast.makeText(this, "Kullanıcı bilgileri bulunamadı.", Toast.LENGTH_SHORT).show();
        }

        konuma_git_butonu.setOnClickListener(v -> {
            Intent konuma_git_butonu = new Intent(ProfileActivity.this,UserMapsActivity.class);
            startActivity(konuma_git_butonu);
        });

        buttonChangePassword.setOnClickListener(v -> {
            Intent changePasswordIntent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            startActivity(changePasswordIntent);
        });

        buttonUpdateProfile.setOnClickListener(v -> {
            Intent updateProfileIntent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
            startActivity(updateProfileIntent);
        });

        buttonLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent loginIntent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            Toast.makeText(ProfileActivity.this, "Çıkış yapıldı.", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserProfile(String email) {
        Gson gson = new Gson();
        String userKey = "user_" + email;
        String userJson = sharedPreferences.getString(userKey, "");
        if (!userJson.isEmpty()) {
            User user = gson.fromJson(userJson, User.class);
            if (user != null) {
                textViewUsername.setText("Kullanıcı Adı: " + user.getName());
                textViewEmail.setText("E-posta: " + user.getEmail());
                textViewPhone.setText("Telefon Numarası: " + user.getNumber());
                textViewCarPlate.setText("Araç Plakası: " + user.getCarPlate());
                textViewCarType.setText("Araç Tipi: " + user.getCarType());
            } else {
                Toast.makeText(this, "Kullanıcı bilgileri yüklenemedi.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Kullanıcı bilgileri bulunamadı.", Toast.LENGTH_SHORT).show();
        }
    }
}
