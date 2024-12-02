package me.gulsum.otopark.UI;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;
=======
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
>>>>>>> 9cf18ff20f6e53bc8bfe893cac2c31d39e010fb5
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
<<<<<<< HEAD
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
=======
    private TextView bosYerTextView;
    private TextView parkKontenjanTextView;
    private TextView priceValueTextView;
    private EditText editTextHours;
    private Button PaymentButton;

    private double totalPrice = 0.0;
>>>>>>> 9cf18ff20f6e53bc8bfe893cac2c31d39e010fb5

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.odeme);

<<<<<<< HEAD
        // View bileşenlerini tanımla
        parkAdiTextView = findViewById(R.id.park_name);
        priceTextView = findViewById(R.id.payment);
        cardNumberEditText = findViewById(R.id.cardNumber);
        cardExpiryEditText = findViewById(R.id.cardExpiry);
        cardCVVEditText = findViewById(R.id.cardCVV);
        paymentButton = findViewById(R.id.buttonPay);
=======
        parkAdiTextView = findViewById(R.id.park_adi);
        bosYerTextView = findViewById(R.id.bosYer);
        parkKontenjanTextView = findViewById(R.id.park_kontenjan);
        priceValueTextView = findViewById(R.id.price_value);
        carTypeSpinner = findViewById(R.id.spinnerCarType);
        editTextHours = findViewById(R.id.editTextHours);
        PaymentButton = findViewById(R.id.confirm_payment_button);
>>>>>>> 9cf18ff20f6e53bc8bfe893cac2c31d39e010fb5

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

        PaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Park bilgilerini ve ödeme detaylarını ProfileActivity'ye gönder
                Intent intent = new Intent(OdemeActivity.this, ProfileActivity.class);

                // Ödeme detaylarını ve park bilgilerini ekle
                intent.putExtra("park_adi", parkAdi); // Park adı
                intent.putExtra("bosYer", bosYer); // Boş yer sayısı
                intent.putExtra("kontenjan", kontenjan); // Kontenjan
                intent.putExtra("totalPrice", totalPrice); // Ödeme tutarı

                // ProfileActivity'ye git
                startActivity(intent);

                // Toast mesajı göster
                Toast.makeText(OdemeActivity.this, "Ödeme talebi alındı", Toast.LENGTH_SHORT).show();
            }
        });
    }

<<<<<<< HEAD
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
=======
    // Fiyatı güncelleyen metod
    private void updatePrice() {
        int[] prices = getResources().getIntArray(R.array.car_prices);
        int position = carTypeSpinner.getSelectedItemPosition();
        double selectedPricePerHour = prices[position];
        String hoursString = editTextHours.getText().toString();
        double hours = 0;
        if (!hoursString.isEmpty()) {
            hours = Double.parseDouble(hoursString);
        }
        totalPrice = selectedPricePerHour * hours;
        priceValueTextView.setText(String.format("%.2f TL", totalPrice));
>>>>>>> 9cf18ff20f6e53bc8bfe893cac2c31d39e010fb5
    }
}
