package com.example.localelection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.List;

public class SuccessRegistration extends AppCompatActivity {
    private VideoView videoView;
    private Uri videoUri;

    @Override
    protected void onResume() {
        super.onResume();
        // Възобновяване на възпроизвеждането на видеото при връщане към приложението
        videoView.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucssess_registration);


        //Подържане на включен екран
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Инициализация на VideoView
        videoView = findViewById(R.id.videoView4);
        // Видео файл за възпроизвеждане
        videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.successpagevideo);
        videoView.setVideoURI(videoUri);
        videoView.start();
        // Слушател за завършване на видеото
        videoView.setOnCompletionListener(mediaPlayer -> {
            videoView.start();
        });
        videoView.start();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public void backToMainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}