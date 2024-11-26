package me.gulsum.otopark.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import me.gulsum.otopark.R;
import me.gulsum.otopark.model.PaymentRequest;
import me.gulsum.otopark.model.PaymentResponse;
import me.gulsum.otopark.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OdemeActivity extends AppCompatActivity {

    private TextView parkAdiTextView;
    private TextView priceTextView;
    private EditText cardNumberEditText;
    private EditText cardExpiryEditText;
    private EditText cardCVVEditText;
    private Button paymentButton;

    private String kullaniciAdi;
    private String kullaniciEmail;
    private String parkAdi;
    private double latitude;
    private double longitude;
    private int kontenjan;
    private int bosYer;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.odeme); // activity_odeme.xml layout dosyasını kullan

        // View bileşenlerini tanımla
        parkAdiTextView = findViewById(R.id.park_adi);
        priceTextView = findViewById(R.id.price_value);
        cardNumberEditText = findViewById(R.id.cardNumber);
        cardExpiryEditText = findViewById(R.id.cardExpiry);
        cardCVVEditText = findViewById(R.id.cardCVV);
        paymentButton = findViewById(R.id.buttonPay);

        // Intent ile gelen verileri al
        kullaniciAdi = getIntent().getStringExtra("kullanici_adi");
        kullaniciEmail = getIntent().getStringExtra("kullanici_email");
        parkAdi = getIntent().getStringExtra("park_adi");
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        kontenjan = getIntent().getIntExtra("kontenjan", 0);
        bosYer = getIntent().getIntExtra("bosYer", 0);
        price = getIntent().getDoubleExtra("price", 0.00);

        // Gelen verileri UI'da göster
        if (parkAdi != null) {
            parkAdiTextView.setText("Park Alanı: " + parkAdi);
        }
        priceTextView.setText("Fiyat: " + price + " TL");

        // Ödeme işlemi
        paymentButton.setOnClickListener(v -> {
            String cardNumber = cardNumberEditText.getText().toString();
            String cardExpiry = cardExpiryEditText.getText().toString();
            String cardCVV = cardCVVEditText.getText().toString();

            if (cardNumber.isEmpty() || cardExpiry.isEmpty() || cardCVV.isEmpty()) {
                Toast.makeText(OdemeActivity.this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
            } else {
                // Ödeme bilgilerini kaydet veya başka bir işlem yap
                savePaymentDetails(cardNumber, cardExpiry, cardCVV);

                // API'yi çağırarak ödeme bilgilerini gönder
                sendPaymentToAPI(cardNumber, cardExpiry, cardCVV);
            }
        });
    }

    // Ödeme bilgilerini kaydetmek için bir metod
    private void savePaymentDetails(String cardNumber, String cardExpiry, String cardCVV) {
        // Bu metodda ödeme bilgilerini kaydedebilirsiniz (örneğin, veritabanına veya bir API'ye gönderebilirsiniz)
        String paymentDetails = "Kart Numarası: " + cardNumber + "\nSon Kullanma Tarihi: " + cardExpiry + "\nCVV: " + cardCVV;
        Toast.makeText(this, "Ödeme Bilgileri: " + paymentDetails, Toast.LENGTH_LONG).show();
    }

    private Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/") // Backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // API'ye ödeme bilgilerini gönderme işlemi
    private void sendPaymentToAPI(String cardNumber, String cardExpiry, String cardCVV) {
        // Burada Retrofit kullanarak API'yi çağırarak ödeme bilgilerini gönderebilirsiniz.
        PaymentRequest paymentRequest = new PaymentRequest(cardNumber, cardExpiry, cardCVV, kullaniciAdi, parkAdi, price);


        // Retrofit API çağrısı yapılırken:
        Retrofit retrofit = getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<PaymentResponse> call = apiService.makePayment(paymentRequest);
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful()) {
                    PaymentResponse paymentResponse = response.body();
                    Toast.makeText(OdemeActivity.this, paymentResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    // Ödeme başarılı olduğunda
                    Intent intent = new Intent(OdemeActivity.this, ProfileActivity.class);
                    intent.putExtra("kullanici_adi", kullaniciAdi);
                    intent.putExtra("kullanici_email", kullaniciEmail);
                    intent.putExtra("park_adi", parkAdi);
                    intent.putExtra("bosYer", bosYer - 1);
                    intent.putExtra("kontenjan", kontenjan);
                    startActivity(intent);
                    finish();
                } else {
                    // Detaylı hata mesajı
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("PaymentError", "Ödeme işlemi başarısız! Hata: " + errorBody);
                        Toast.makeText(OdemeActivity.this, "Ödeme işlemi başarısız! Hata: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("PaymentError", "Hata: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Toast.makeText(OdemeActivity.this, "Hata: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
