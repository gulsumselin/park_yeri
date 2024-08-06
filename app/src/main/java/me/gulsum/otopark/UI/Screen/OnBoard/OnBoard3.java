package me.gulsum.otopark.UI.Screen.OnBoard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.gulsum.otopark.R;
import me.gulsum.otopark.UI.SelectionActivity;

public class OnBoard3 extends AppCompatActivity {
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboard_3);

        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoard3.this, SelectionActivity.class);
                startActivity(intent);
            }
        });
    }
}

