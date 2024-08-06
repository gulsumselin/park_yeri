package me.gulsum.otopark.UI.Screen.OnBoard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.gulsum.otopark.R;

public class OnBoard2 extends AppCompatActivity {
    private Button btnNext2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboard_2);

        btnNext2 = findViewById(R.id.btnNext2);

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoard2.this, OnBoard3.class);
                startActivity(intent);
            }
        });
    }
}

