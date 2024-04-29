package com.example.localelection;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;


public class LoginActivity extends AppCompatActivity {

    private static EditText loginEmail;
    private EditText password;
    private TextView loginErrorText;
    private VideoView videoView;
    private Uri videoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Подържане на включен екран
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Инициализация на VideoView
        videoView = findViewById(R.id.videoView5);
        // Видео файл за възпроизвеждане
        videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loginpagevideo);
        videoView.setVideoURI(videoUri);
        videoView.start();
        // Слушател за завършване на видеото
        videoView.setOnCompletionListener(mediaPlayer -> {
            videoView.start();
        });
        videoView.start();


        loginEmail = findViewById(R.id.loginEmailFeald);
        password = findViewById(R.id.LoginPasswordField);
        loginErrorText = findViewById(R.id.loginErrorText);

        Button agreeButton = findViewById(R.id.LoginAgreeButton);
        agreeButton.setOnClickListener(v -> {
            String enteredEmail = loginEmail.getText().toString();
            String enteredPassword = password.getText().toString();

            if (TextUtils.isEmpty(enteredEmail)) {
                loginErrorText.setText("Моля, попълнете полето за електронна поща.");
            } else if (TextUtils.isEmpty(enteredPassword)) {
                loginErrorText.setText("Моля, попълнете полето с вашата парола");
            } else {
                new SendLoginTask().execute(enteredEmail, enteredPassword);
                //Синхрон със Сървъра
                try {
                    Thread.sleep(1000); // Изчакване за 1 сек
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleServerResponse(String result) {
        result = MyWebSocketClient.getLoginResult();

        if ("-1".equals(result)) {
            System.out.println("Successful login. Redirecting to verification activity.");
            Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
            startActivity(intent);
            finish();

        } else if ("-0".equals(result)) {
            runOnUiThread(() -> loginErrorText.setText("Няма регистриран гласоподавател със същите данни"));

        } else {
            runOnUiThread(() -> loginErrorText.setText("Потвърдете отново!"));

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class SendLoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String enteredEmail = params[0];
            String enteredPassword = params[1];
            return MyApplication.sendLoginCheck("loginResult:" + enteredEmail + ":" + enteredPassword);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("e tva idva na onPost"+result);
            handleServerResponse(result);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
