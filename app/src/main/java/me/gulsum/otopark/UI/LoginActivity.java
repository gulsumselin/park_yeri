package me.gulsum.otopark.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import me.gulsum.otopark.R;
import me.gulsum.otopark.model.LoginRequest;
import me.gulsum.otopark.model.LoginResponse;
import me.gulsum.otopark.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Login butonuna tıklanma olayı
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        // E-posta ve şifreyi al
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        // Alanların boş olup olmadığını kontrol et
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, password);

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000") // Backend API URL'si
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // API çağrısını yap
        Call<LoginResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    // API yanıtı başarılıysa
                    LoginResponse loginResponse = response.body();
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    // Kullanıcıyı ProfileActivity'ye yönlendir
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    // Gerekli bilgileri ilet
                    intent.putExtra("user_email", email);
                    startActivity(intent);
                    finish(); // Giriş başarılı, LoginActivity'yi kapat
                } else {
                    // Hata durumunda alınan mesaj
                    String errorMessage = "Geçersiz e-posta veya şifre";
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        errorMessage = errorObject.optString("error", "Bilinmeyen bir hata oluştu");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // API isteği başarısız olursa
                Toast.makeText(LoginActivity.this, "Bir hata oluştu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
