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
        String number = editTextPhone.getText().toString();
        String password = editTextPassword.getText().toString();
        String carPlate = editTextCarPlate.getText().toString();
        String carType = spinnerCarType.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || carPlate.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(name, email, number, password, carPlate, carType);
        saveUser(user);

        Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveUser(User user) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String userKey = "user_" + user.getEmail();
        String userJson = gson.toJson(user);
        editor.putString(userKey, userJson);
        editor.apply();
    }
}
