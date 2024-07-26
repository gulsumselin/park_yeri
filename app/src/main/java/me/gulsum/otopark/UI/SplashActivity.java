package me.gulsum.otopark.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import me.gulsum.otopark.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent onBoardIntent = new Intent(SplashActivity.this, SelectionActivity.class);
            startActivity(onBoardIntent);
            finish();
        }, SPLASH_DELAY);
    }
}