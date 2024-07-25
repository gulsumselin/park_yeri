package me.gulsum.otopark.UI;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import me.gulsum.otopark.R;

public class RezervasyonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rezervasyon);

        String parkAdi = getIntent().getStringExtra("park_adi");
        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);

        TextView parkAdiTextView = findViewById(R.id.park_adi);
        TextView koordinatlarTextView = findViewById(R.id.koordinatlar);

        parkAdiTextView.setText("Park Alanı: " + parkAdi);
        koordinatlarTextView.setText("Koordinatlar: " + latitude + ", " + longitude);


    }
}
