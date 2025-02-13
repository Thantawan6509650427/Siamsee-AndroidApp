package com.example.assignment3;

import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

public class FortuneActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private static final int SHAKE_THRESHOLD = 10; // ลดค่า threshold ลงเพื่อให้ไวขึ้น
    private boolean shownDialog = false;
    private String[] fortuneMessages = {
            "โชคดีมาก", "พบลาภลอย", "ระวังปัญหา", "อดทนแล้วจะสำเร็จ", "พบเจอความรักใหม่"
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // กลับไปหน้าหลัก
            Intent intent = new Intent(this, MainActivity.class); // สมมติว่าหน้าหลักคือ MainActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortune);

        // ตั้งค่า Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // หา FloatingActionButton จาก Layout
        FloatingActionButton btnBackToHome = findViewById(R.id.btn_backtohome);

        // ตั้งค่า OnClickListener
        btnBackToHome.setOnClickListener(v -> {
            // ใช้ Intent เพื่อกลับไปยังหน้าหลัก
            Intent intent = new Intent(FortuneActivity.this, MainActivity.class); // แก้เป็น MainActivity หรือหน้าหลักที่ต้องการ
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // หา VideoView จาก layout
        VideoView videoView = findViewById(R.id.videoView);

        // กำหนดแหล่งที่มาของวิดีโอจาก res/raw
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.my_video);
        videoView.setVideoURI(videoUri);

        // ตั้งค่าให้ MediaPlayer เล่นวนซ้ำ
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();
            }
        });

        // เริ่มเล่นวิดีโอ
        videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager == null) {
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        }

        if (sensorManager != null) {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float accX = event.values[0];
            float accY = event.values[1];
            float accZ = event.values[2];

            Log.d("ShakeDetection", "accX: " + accX + " accY: " + accY + " accZ: " + accZ);

            // ใช้ Thread ในการตรวจจับการเขย่า
            new Thread(() -> detectShake("X", accX)).start();
            new Thread(() -> detectShake("Y", accY)).start();
            new Thread(() -> detectShake("Z", accZ)).start();
        }
    }

    private void detectShake(String axis, float acceleration) {
        if (Math.abs(acceleration) > SHAKE_THRESHOLD && !shownDialog) {
            shownDialog = true;

            switch (axis) {
                case "X":
                    Log.d("ShakeDetection", "Shake Detected: X-Axis");
                    break;
                case "Y":
                    Log.d("ShakeDetection", "Shake Detected: Y-Axis");
                    break;
                case "Z":
                    Log.d("ShakeDetection", "Shake Detected: Z-Axis");
                    break;
            }

            // เรียกใช้ handleShake() เพื่อแสดงผล
            runOnUiThread(this::handleShake);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ไม่จำเป็นต้องทำอะไรในที่นี้
    }

    private void handleShake() {
        // สุ่มเลข (จาก 0-4) เพื่อเลือกคำทำนาย
        Random random = new Random();
        int randomNumber = random.nextInt(fortuneMessages.length);
        String fortune = fortuneMessages[randomNumber];

        // สร้าง Layout แบบกำหนดเองสำหรับข้อความใน AlertDialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 40);  // ตั้งค่า padding รอบข้อความ
        layout.setGravity(Gravity.CENTER);  // จัดข้อความให้อยู่ตรงกลาง

        // TextView สำหรับแสดงเลขที่สุ่มได้
        TextView numberTextView = new TextView(this);
        numberTextView.setText(String.valueOf(randomNumber + 1));  // แสดงเลขที่สุ่มได้
        numberTextView.setTextSize(24);  // ขนาดตัวอักษรใหญ่
        numberTextView.setGravity(Gravity.CENTER);  // จัดให้อยู่ตรงกลาง
        numberTextView.setPadding(0, 20, 0, 10);  // ตั้งค่า padding (บน, ล่าง)

        // TextView สำหรับแสดงคำทำนาย
        TextView fortuneTextView = new TextView(this);
        fortuneTextView.setText("โชคของคุณ: " + fortune);  // แสดงคำทำนาย
        fortuneTextView.setTextSize(18);  // ขนาดตัวอักษรรองลงมา
        fortuneTextView.setGravity(Gravity.CENTER);  // จัดให้อยู่ตรงกลาง
        fortuneTextView.setPadding(0, 10, 0, 20);  // ตั้งค่า padding (บน, ล่าง)

        // เพิ่ม TextView ทั้งสองลงใน Layout
        layout.addView(numberTextView);
        layout.addView(fortuneTextView);

        // สร้าง AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ผลเซียมซี")  // Title ของ Dialog
                .setView(layout) // ใช้ Layout แบบกำหนดเอง
                .setPositiveButton("รับคำทำนาย", (dialog, which) -> {
                    dialog.dismiss();  // ปิด dialog เมื่อกดปุ่ม OK
                    shownDialog = false; // ปลดล็อกเพื่อให้สามารถแสดงผลอีกครั้ง
                });

        // แสดง Dialog
        builder.create().show();
    }
}
