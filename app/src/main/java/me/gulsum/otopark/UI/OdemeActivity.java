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
import java.time.LocalDateTime;

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
    private String userName;
    private String email;
    private String parkName;
    private int kontenjan;
    private int bosYer;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.odeme);

        // View bileşenlerini tanımla
        parkAdiTextView = findViewById(R.id.park_name);
        priceTextView = findViewById(R.id.payment);
        cardNumberEditText = findViewById(R.id.cardNumber);
        cardExpiryEditText = findViewById(R.id.cardExpiry);
        cardCVVEditText = findViewById(R.id.cardCVV);
        paymentButton = findViewById(R.id.buttonPay);

        // Intent ile gelen verileri al
        userName = getIntent().getStringExtra("user_name");
        email = getIntent().getStringExtra("user_email");
        parkName = getIntent().getStringExtra("park_name");
        kontenjan = getIntent().getIntExtra("kontenjan", 0);
        bosYer = getIntent().getIntExtra("bosYer", 0);
        price = getIntent().getDoubleExtra("price", 0.00);

        // Gelen verileri UI'da göster
        if (parkName != null) {
            parkAdiTextView.setText("Park Alanı: " + parkName);
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
                // Ödeme bilgilerini gönder
                sendPaymentToAPI(email, cardNumber, cardExpiry, cardCVV);
            }
        });
    }

    private Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/") // Backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void sendPaymentToAPI(String email, String cardNumber, String cardExpiry, String cardCVV) {
        // Sistem tarafından oluşturulan ödeme bilgileri
        long createdAt = System.currentTimeMillis(); // Ödeme tarihi
        String paymentId = null; // Başlangıçta null, sunucudan alınacak

        // Ödeme isteği oluştur
        PaymentRequest paymentRequest = new PaymentRequest(
                email, cardNumber, cardExpiry, cardCVV, paymentId, parkName, price, createdAt
        );

        // API çağrısı yap
        Retrofit retrofit = getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<PaymentResponse> call = apiService.makePayment(paymentRequest);
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful()) {
                    PaymentResponse paymentResponse = response.body();
                    if (paymentResponse != null) {
                        String generatedPaymentId = paymentResponse.getPaymentId(); // Sunucudan dönen paymentId
                        Toast.makeText(OdemeActivity.this, "Ödeme Başarılı! Payment ID: " + generatedPaymentId, Toast.LENGTH_SHORT).show();

                        // Başarılı işlem sonrası ProfileActivity'ye geçiş
                        Intent intent = new Intent(OdemeActivity.this, ProfileActivity.class);
                        intent.putExtra("user_name", userName);
                        intent.putExtra("user_email", email);
                        intent.putExtra("park_name", parkName);
                        intent.putExtra("bosYer", bosYer - 1);
                        intent.putExtra("kontenjan", kontenjan);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("PaymentError", "Ödeme işlemi başarısız! Hata: " + errorBody);
                        Toast.makeText(OdemeActivity.this, "Hata: " + errorBody, Toast.LENGTH_LONG).show();
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
