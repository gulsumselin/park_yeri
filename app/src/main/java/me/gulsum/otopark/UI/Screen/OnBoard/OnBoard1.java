package me.gulsum.otopark.UI.Screen.OnBoard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.gulsum.otopark.R;

public class OnBoard1 extends AppCompatActivity {
    private Button btnNext1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.onboard_1);

            btnNext1 = findViewById(R.id.btnNext1);

            btnNext1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OnBoard1.this, OnBoard2.class);
                    startActivity(intent);
                }
            });
        }
    }

