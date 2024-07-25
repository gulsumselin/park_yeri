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
        int kontenjan = getIntent().getIntExtra("kontenjan",0);
        int giren = getIntent().getIntExtra("giren",0);
        int bosYer = getIntent().getIntExtra("bosYer",0);

        TextView parkAdiTextView = findViewById(R.id.park_adi);
        TextView koordinatlarTextView = findViewById(R.id.koordinatlar);
        TextView kontenjanTextView = findViewById(R.id.kontenjan);
        TextView bosYerTextView = findViewById(R.id.bosYer);

        parkAdiTextView.setText("Park Alanı: " + parkAdi);
        koordinatlarTextView.setText("Koordinatlar: " + latitude + ", " + longitude);
        kontenjanTextView.setText("Kontenjan:" + kontenjan);
        bosYerTextView.setText("Bos Yer:" + bosYer);


    }
}
