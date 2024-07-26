package me.gulsum.otopark.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import me.gulsum.otopark.R;

public class RezervasyonActivity extends AppCompatActivity {

    private TextView parkAdiTextView;
    private TextView bosYerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rezervasyon);

        parkAdiTextView = findViewById(R.id.park_adi);
        bosYerTextView = findViewById(R.id.bosYer);

        String parkAdi = getIntent().getStringExtra("park_adi");
        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);
        int kontenjan = getIntent().getIntExtra("kontenjan", 0);
        int giren = getIntent().getIntExtra("giren", 0);
        int bosYer = getIntent().getIntExtra("bosYer", 0);

        showBottomSheetDialog(parkAdi, latitude, longitude, kontenjan, bosYer);
    }

    private void showBottomSheetDialog(String parkAdi, double latitude, double longitude, int kontenjan, int bosYer) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

        TextView bottomSheetParkAdiTextView = bottomSheetDialog.findViewById(R.id.park_adi);
        TextView koordinatlarTextView = bottomSheetDialog.findViewById(R.id.koordinatlar);
        TextView kontenjanTextView = bottomSheetDialog.findViewById(R.id.kontenjan);
        TextView bottomSheetBosYerTextView = bottomSheetDialog.findViewById(R.id.bosYer);
        Button konumaGitButton = bottomSheetDialog.findViewById(R.id.konuma_git_button);

        if (bottomSheetParkAdiTextView != null) bottomSheetParkAdiTextView.setText("Park Alanı: " + parkAdi);
        if (koordinatlarTextView != null) koordinatlarTextView.setText("Koordinatlar: " + latitude + ", " + longitude);
        if (kontenjanTextView != null) kontenjanTextView.setText("Kontenjan: " + kontenjan);
        if (bottomSheetBosYerTextView != null) bottomSheetBosYerTextView.setText("Boş Yer: " + bosYer);

        if (konumaGitButton != null) {
            konumaGitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + parkAdi + ")");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            });
        }

        bottomSheetDialog.setOnDismissListener(dialog -> {
            parkAdiTextView.setText("Park Alanı: " + parkAdi);
            bosYerTextView.setText("Boş Yer: " + bosYer);
        });

        bottomSheetDialog.show();
    }
}
