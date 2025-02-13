package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ตั้งค่า Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ปุ่มสำหรับเปิด FortuneActivity
        Button openFortuneButton = findViewById(R.id.btnOpenFortune);
        openFortuneButton.setOnClickListener(v -> {
            // เปิด FortuneActivity
            Intent intent = new Intent(MainActivity.this, FortuneActivity.class);
            startActivity(intent);
        });

    }
}

