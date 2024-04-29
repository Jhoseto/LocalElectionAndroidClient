package com.example.localelection;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
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

public class VerificationActivity extends AppCompatActivity {

    private static EditText verCode;
    private TextView verErrorText;
    private VideoView videoView;
    private Uri videoUri;

    public static String getVerCode() {
        return verCode.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        //Подържане на включен екран
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        videoView = findViewById(R.id.videoView2);
        videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vercodee);
        videoView.setVideoURI(videoUri);

        // Слушател за завършване на видеото
        videoView.setOnCompletionListener(mediaPlayer -> {
            videoView.start();
        });
        videoView.start();

        verCode = findViewById(R.id.verificationCodeFeald);
        verErrorText = findViewById(R.id.verErrorText);

        Button verAgreeButton = findViewById(R.id.verificationAgreeButton);
        verAgreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = verCode.getText().toString();

                if (TextUtils.isEmpty(enteredCode)) {
                    verErrorText.setText("Моля, попълнете полето с вашият ВЕРИФИКАЦИОНЕН КОД");
                } else {
                    new SendVerifikationTask().execute(enteredCode);
                    //Синхрон със Сървъра
                    try {
                        Thread.sleep(1000); // Изчакване за 1 сек
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Възобновяване на възпроизвеждането на видеото при връщане към приложението
        videoView.start();
    }

    @SuppressLint("SetTextI18n")
    private void handleServerResponse() {
        String result = MyWebSocketClient.getVerResult();
        System.out.println("e tva idva na kura mi "+result);

        //Първата цифра означава дали кода зъществува а втората даливече е ползван
        if ("10".equals(result)) {
            System.out.println("Successful. Redirecting to VOTE activity.");
            Intent intent = new Intent(VerificationActivity.this, VoteActivity.class);
            startActivity(intent);
            finish();
        } else if ("11".equals(result)) {
            runOnUiThread(() -> verErrorText.setText("Вече е упражнен вот със\n " +
                    "този ВЕРИФИКАЦИОНЕН КОД. \nНямате право да го използвате повторно"));


        } else if ("0".equals(result)) {
            runOnUiThread(() -> verErrorText.setText("Невалиден КОД"));

        } else {
            runOnUiThread(() -> verErrorText.setText("Потвърдете отново!"));

        }
    }


    private class SendVerifikationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String enteredVerCode = params[0];
            return MyApplication.sendVerificationCheck("verResult:" + enteredVerCode);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("e tva idva na onPost"+result);
            handleServerResponse();
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