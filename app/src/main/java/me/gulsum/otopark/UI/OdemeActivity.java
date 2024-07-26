package me.gulsum.otopark.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import me.gulsum.otopark.R;

public class OdemeActivity extends AppCompatActivity{


        private TextView userNameTextView;
        private TextView userEmailTextView;
        private TextView parkAdiTextView;
        private TextView bosYerTextView;
        private TextView parkKontenjanTextView;
        private TextView priceValueTextView;
        private Button confirmPaymentButton;

        private SharedPreferences sharedPreferences;
        private static final String PREF_NAME = "user_prefs";
        private static final String KEY_LOGGED_IN_USER = "logged_in_user";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.odeme);

            userNameTextView = findViewById(R.id.user_name);
            userEmailTextView = findViewById(R.id.user_email);
            parkAdiTextView = findViewById(R.id.park_adi);
            bosYerTextView = findViewById(R.id.bosYer);
            parkKontenjanTextView = findViewById(R.id.park_kontenjan);
            priceValueTextView = findViewById(R.id.price_value);
            confirmPaymentButton = findViewById(R.id.confirm_payment_button);

            sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            loadUserData();

            String parkAdi = "Örnek Park Alanı";
            double latitude = 41.015137;
            double longitude = 28.979530;
            int kontenjan = 100;
            int bosYer = 20;
            double price = 50.00;

            parkAdiTextView.setText("Park Alanı: " + parkAdi);
            bosYerTextView.setText("Boş Yer: " + bosYer);
            parkKontenjanTextView.setText("Kontenjan: " + kontenjan);
            priceValueTextView.setText(price + " TL");

            confirmPaymentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OdemeActivity.this, "Ödeme onaylandı!", Toast.LENGTH_SHORT).show();

                }
            });
        }

        private void loadUserData() {
            Gson gson = new Gson();
            String json = sharedPreferences.getString(KEY_LOGGED_IN_USER, "");
            if (!json.isEmpty()) {
                User loggedInUser = gson.fromJson(json, User.class);
                userNameTextView.setText("Ad: " + loggedInUser.getName());
                userEmailTextView.setText("E-posta: " + loggedInUser.getEmail());
            } else {
                Intent intent = new Intent(OdemeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

