package me.gulsum.otopark.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import me.gulsum.otopark.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER_LIST = "user_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_USER_LIST, "[]");

        Type type = new TypeToken<List<User>>() {}.getType();
        List<User> userList = gson.fromJson(json, type);

        boolean isValidUser = false;
        for (User user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                isValidUser = true;
                break;
            }
        }

        if (isValidUser) {
            Toast.makeText(this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, UserMapsActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Geçersiz e-posta veya şifre.", Toast.LENGTH_SHORT).show();
        }
    }
}
