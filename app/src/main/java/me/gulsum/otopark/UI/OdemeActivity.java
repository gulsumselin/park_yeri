package me.gulsum.otopark.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.gulsum.otopark.R;

public class OdemeActivity extends AppCompatActivity {

    private Spinner carTypeSpinner;
    private TextView parkAdiTextView;
    private TextView bosYerTextView;
    private TextView parkKontenjanTextView;
    private TextView priceValueTextView;
    private EditText editTextHours;
    private Button PaymentButton;

    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.odeme);

        parkAdiTextView = findViewById(R.id.park_adi);
        bosYerTextView = findViewById(R.id.bosYer);
        parkKontenjanTextView = findViewById(R.id.park_kontenjan);
        priceValueTextView = findViewById(R.id.price_value);
        carTypeSpinner = findViewById(R.id.spinnerCarType);
        editTextHours = findViewById(R.id.editTextHours);
        PaymentButton = findViewById(R.id.confirm_payment_button);

        Intent intent = getIntent();
        String kullaniciAdi = intent.getStringExtra("kullanici_adi");
        String kullaniciEmail = intent.getStringExtra("kullanici_email");
        String parkAdi = intent.getStringExtra("park_adi");
        int bosYer = intent.getIntExtra("bosYer", 0);
        int kontenjan = intent.getIntExtra("kontenjan", 0);
        double initialPrice = intent.getDoubleExtra("price", 0.0);

        parkAdiTextView.setText("Park Alanı: " + parkAdi);
        bosYerTextView.setText("Boş Yer: " + bosYer);
        parkKontenjanTextView.setText("Kontenjan: " + kontenjan);
        priceValueTextView.setText(String.format("%.2f TL", initialPrice));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.car_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carTypeSpinner.setAdapter(adapter);

        carTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do
            }
        });

        editTextHours.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePrice();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
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
    }
}
