package com.example.localelection;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Подържане на включен екран
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Инициализация на VideoView
        videoView = findViewById(R.id.videoView);
        // Видео файл за възпроизвеждане
        videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoView.setVideoURI(videoUri);
        videoView.start();

        // Стартиране на BackgroundMusicService
       Intent intent = new Intent(this, BackgroundMusicService.class);
       startService(intent);

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Възобновяване на възпроизвеждането на видеото при връщане към приложението
        videoView.start();
    }
    public void navigateToLoginPage(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void navigateToRegistrationPage(View view) {
        Intent intent = new Intent(this, RegistrationPage.class);
        startActivity(intent);
        finish();
    }
}

