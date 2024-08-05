package me.gulsum.otopark.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.gulsum.otopark.R;

public class OdemeActivity extends AppCompatActivity {

    private Spinner carTypeSpinner;
    private TextView parkAdiTextView;
    private TextView bosYerTextView;
    private TextView parkKontenjanTextView;
    private TextView priceValueTextView;
    private EditText editTextHours;

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
    }

    private void updatePrice() {
        int[] prices = getResources().getIntArray(R.array.car_prices);
        int position = carTypeSpinner.getSelectedItemPosition();
        double selectedPricePerHour = prices[position];
        String hoursString = editTextHours.getText().toString();
        double hours = 0;
        if (!hoursString.isEmpty()) {
            hours = Double.parseDouble(hoursString);
        }
        double totalPrice = selectedPricePerHour * hours;
        priceValueTextView.setText(String.format("%.2f TL", totalPrice));
    }
}
