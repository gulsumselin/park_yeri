package me.gulsum.otopark.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import me.gulsum.otopark.R;
import me.gulsum.otopark.model.RegisterRequest;
import me.gulsum.otopark.model.RegisterResponse;
import me.gulsum.otopark.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone, editTextPassword, editTextCarPlate;
    private Spinner spinnerCarType;
    private Button buttonRegister;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER_LIST = "user_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextCarPlate = findViewById(R.id.editTextCarPlate);
        spinnerCarType = findViewById(R.id.spinnerCarType);
        buttonRegister = findViewById(R.id.buttonRegister);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String password = editTextPassword.getText().toString();
        String carPlate = editTextCarPlate.getText().toString();
        String carType = spinnerCarType.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || carPlate.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simple email validation
        if (!email.contains("@")) {
            Toast.makeText(this, "Geçersiz email adresi.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create register request object
        RegisterRequest registerRequest = new RegisterRequest(name, email, phone, password, carPlate, carType);

        // Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000") // Update with actual backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<RegisterResponse> call = apiService.registerUser(registerRequest);

        // Call API and handle response
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    // Save user details to SharedPreferences after successful registration
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse != null) {
                        saveUser(registerResponse);
                    }
                    Toast.makeText(RegisterActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Kayıt işlemi başarısız.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Bir hata oluştu.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUser(RegisterResponse user) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String userKey = "user_" + user.getEmail();
        String userJson = gson.toJson(user);
        editor.putString(userKey, userJson);
        editor.apply();
    }
}
