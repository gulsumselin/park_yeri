package me.gulsum.otopark.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import me.gulsum.otopark.R;
import me.gulsum.otopark.model.LoginResponse;
import me.gulsum.otopark.model.ReservationRequest;
import me.gulsum.otopark.model.ReservationResponse;
import me.gulsum.otopark.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRezervasyonActivity extends AppCompatActivity {

    private static final String TAG = "UserRezervasyonActivity";
    private TextView parkNameTextView;
    private TextView bosYerTextView;
    private int bosYer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rezervasyon_user);

        // View bileşenlerini tanımla
        parkNameTextView = findViewById(R.id.park_name);
        bosYerTextView = findViewById(R.id.bosYer);
        Button rezervasyonButton = findViewById(R.id.rezervasyon);

        // Intent ile gelen verileri al
        String parkName = getIntent().getStringExtra("park_name");
        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);
        int kontenjan = getIntent().getIntExtra("kontenjan", 0);
        bosYer = getIntent().getIntExtra("bosYer", 0);
        String userName = getIntent().getStringExtra("user_name");
        String entryTime = getIntent().getStringExtra("entry_time");
        double payment = getIntent().getDoubleExtra("payment", 0);
        String email = getIntent().getStringExtra("user_email");
        String reservationId = getIntent().getStringExtra("reservation_id");

        Log.d(TAG, "Intent verileri alındı: parkName=" + parkName + ", bosYer=" + bosYer);

        // Gelen verileri UI'da göster
        if (parkName!= null) {
            parkNameTextView.setText("Park Alanı: " + parkName);
        }
        if (bosYer > 0) {
            bosYerTextView.setText("Boş Yer: " + bosYer);
        } else {
            bosYerTextView.setText("Boş Yer: Yok");
            rezervasyonButton.setEnabled(false); // Rezervasyon butonunu devre dışı bırak
        }

        // Bottom Sheet gösterimi
        showBottomSheetDialog(parkName, latitude, longitude, kontenjan, bosYer);

        rezervasyonButton.setOnClickListener(v -> {
            Log.d(TAG, "Rezervasyon butonuna tıklandı.");

            ReservationRequest reservationRequest = new ReservationRequest(
                    email, parkName, reservationId, entryTime, payment
            );

            if (email != null && !email.isEmpty()) {
                saveRezervasyonToDatabase(reservationRequest);

                if (bosYer > 0) {
                    bosYer--;
                    bosYerTextView.setText("Boş Yer: " + bosYer);

                    Intent intent = new Intent(UserRezervasyonActivity.this, OdemeActivity.class);
                    intent.putExtra("user_name", userName);
                    intent.putExtra("email", email);
                    intent.putExtra("reservation_id", reservationId);
                    intent.putExtra("park_name", parkName);
                    intent.putExtra("bosYer", bosYer);
                    intent.putExtra("kontenjan", kontenjan);
                    intent.putExtra("price", 50.00);
                    startActivity(intent);

                    Toast.makeText(UserRezervasyonActivity.this, "Rezervasyon başarılı!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserRezervasyonActivity.this, "Boş yer kalmadı!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Kullanıcı bilgileri bulunamadı.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Kullanıcı emaili bulunamadı.");
            }
        });
    }

    private Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/") // Backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



    private void saveRezervasyonToDatabase(ReservationRequest reservationRequest) {
        ApiService apiService = getRetrofitInstance().create(ApiService.class);
        Call<ReservationResponse> call = apiService.saveReservationToDatabase(reservationRequest);

        Log.d(TAG, "Rezervasyon kaydetme isteği gönderiliyor: " + reservationRequest);

        call.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Rezervasyon kaydedildi: " + response.body());
                    Toast.makeText(UserRezervasyonActivity.this, "Rezervasyon başarıyla kaydedildi!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Rezervasyon kaydetme hatası: " + response.errorBody());
                    Toast.makeText(UserRezervasyonActivity.this, "Hata: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t) {
                Log.e(TAG, "Sunucuya bağlanılamadı: ", t);
                Toast.makeText(UserRezervasyonActivity.this, "Sunucuya bağlanılamadı: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showBottomSheetDialog(String parkName, double latitude, double longitude, int kontenjan, int bosYer) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

        TextView bottomSheetParkAdiTextView = bottomSheetDialog.findViewById(R.id.park_name);
        TextView koordinatlarTextView = bottomSheetDialog.findViewById(R.id.koordinatlar);
        TextView kontenjanTextView = bottomSheetDialog.findViewById(R.id.kontenjan);
        TextView bosYerTextView = bottomSheetDialog.findViewById(R.id.bosYer);

        if (bottomSheetParkAdiTextView != null) {
            bottomSheetParkAdiTextView.setText(parkName);
        }
        if (koordinatlarTextView != null) {
            koordinatlarTextView.setText("Latitude: " + latitude + " | Longitude: " + longitude);
        }
        if (kontenjanTextView != null) {
            kontenjanTextView.setText("Kontenjan: " + kontenjan);
        }
        if (bosYerTextView != null) {
            bosYerTextView.setText("Boş Yer: " + bosYer);
        }

        Button konumaGitButton = bottomSheetDialog.findViewById(R.id.konuma_git_button);
        if (konumaGitButton != null) {
            konumaGitButton.setOnClickListener(v -> {
                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            });
        }

        bottomSheetDialog.show();
    }
}
