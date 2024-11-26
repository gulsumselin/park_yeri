package me.gulsum.otopark.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import me.gulsum.otopark.R;

public class UserRezervasyonActivity extends AppCompatActivity {

    private TextView parkAdiTextView;
    private TextView bosYerTextView;
    private int bosYer; // Dinamik kontrol için boş yer değişkeni

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rezervasyon_user);

        // View bileşenlerini tanımla
        parkAdiTextView = findViewById(R.id.park_adi);
        bosYerTextView = findViewById(R.id.bosYer);
        Button rezervasyonButton = findViewById(R.id.rezervasyon);

        // Intent ile gelen verileri al
        String parkAdi = getIntent().getStringExtra("park_adi");
        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);
        int kontenjan = getIntent().getIntExtra("kontenjan", 0);
        bosYer = getIntent().getIntExtra("bosYer", 0);
        String kullaniciAdi = getIntent().getStringExtra("kullanici_adi");
        String kullaniciEmail = getIntent().getStringExtra("kullanici_email");

        // Gelen verileri UI'da göster
        if (parkAdi != null) {
            parkAdiTextView.setText("Park Alanı: " + parkAdi);
        }
        if (bosYer > 0) {
            bosYerTextView.setText("Boş Yer: " + bosYer);
        } else {
            bosYerTextView.setText("Boş Yer: Yok");
            rezervasyonButton.setEnabled(false); // Rezervasyon butonunu devre dışı bırak
        }

        // Bottom Sheet gösterimi
        showBottomSheetDialog(parkAdi, latitude, longitude, kontenjan, bosYer);

        // Rezervasyon yapma işlemi
        rezervasyonButton.setOnClickListener(v -> {
            if (bosYer > 0) {
                bosYer--; // Rezervasyon sonrası boş yer sayısını azalt
                bosYerTextView.setText("Boş Yer: " + bosYer);
                Toast.makeText(UserRezervasyonActivity.this, "Rezervasyon başarılı!", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(UserRezervasyonActivity.this, OdemeActivity.class);
                intent.putExtra("kullanici_adi", kullaniciAdi);
                intent.putExtra("kullanici_email", kullaniciEmail);
                intent.putExtra("park_adi", parkAdi);
                intent.putExtra("bosYer", bosYer);
                intent.putExtra("kontenjan", kontenjan);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("price", 50.00); // Sabit fiyat
                startActivity(intent);
            } else {
                Toast.makeText(UserRezervasyonActivity.this, "Boş yer kalmadı!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBottomSheetDialog(String parkAdi, double latitude, double longitude, int kontenjan, int bosYer) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

        // Bottom Sheet içeriğini tanımla
        TextView bottomSheetParkAdiTextView = bottomSheetDialog.findViewById(R.id.park_adi);
        TextView koordinatlarTextView = bottomSheetDialog.findViewById(R.id.koordinatlar);
        TextView kontenjanTextView = bottomSheetDialog.findViewById(R.id.kontenjan);
        TextView bottomSheetBosYerTextView = bottomSheetDialog.findViewById(R.id.bosYer);
        Button konumaGitButton = bottomSheetDialog.findViewById(R.id.konuma_git_button);

        if (bottomSheetParkAdiTextView != null) {
            bottomSheetParkAdiTextView.setText("Park Alanı: " + parkAdi);
        }
        if (koordinatlarTextView != null) {
            koordinatlarTextView.setText(String.format("Koordinatlar: %.6f, %.6f", latitude, longitude));
        }
        if (kontenjanTextView != null) {
            kontenjanTextView.setText("Kontenjan: " + kontenjan);
        }
        if (bottomSheetBosYerTextView != null) {
            bottomSheetBosYerTextView.setText("Boş Yer: " + bosYer);
        }

        if (konumaGitButton != null) {
            konumaGitButton.setOnClickListener(v -> {
                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + parkAdi + ")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent); // Haritayı başlat
                }
            });
        }

        bottomSheetDialog.show();
    }
}
