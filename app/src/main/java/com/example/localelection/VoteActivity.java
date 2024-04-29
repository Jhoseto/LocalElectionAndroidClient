package com.example.localelection;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;


public class VoteActivity extends AppCompatActivity {

    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private Button voteButton;
    private TextView voteErrorText;
    private VideoView videoView;
    private Uri videoUri;

    private int selectedCandidateId = -1; // Избран кандидат, -1 означава неизбран

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        //Подържане на включен екран
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        videoView = findViewById(R.id.videoView7);
        videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loginpagevideo);
        videoView.setVideoURI(videoUri);

        // Слушател за завършване на видеото
        videoView.setOnCompletionListener(mediaPlayer -> {
            videoView.start();
        });
        videoView.start();

        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        voteButton = findViewById(R.id.voteButton);
        voteErrorText = findViewById(R.id.voteErrorText);


        // Слушатели за избор на кандидат
        radioButton1.setOnClickListener(v -> onRadioButtonClicked(1));
        radioButton2.setOnClickListener(v -> onRadioButtonClicked(2));
        radioButton3.setOnClickListener(v -> onRadioButtonClicked(3));

        // Слушател за потвърждение на гласуването
        voteButton.setOnClickListener(v -> onVoteButtonClicked());
    }

    private void onRadioButtonClicked(int candidateId) {
        // При избиране на кандидат се актуализира променливата selectedCandidateId
        selectedCandidateId = candidateId;
    }

    private void onVoteButtonClicked() {
        if (selectedCandidateId != -1) {
            // Изпращане на избора на сървъра
            SendVoteTask sendVoteTask = new SendVoteTask();
            sendVoteTask.execute();

            // Деактивиране на радио бутоните и бутона за гласуване
            radioButton1.setEnabled(false);
            radioButton2.setEnabled(false);
            radioButton3.setEnabled(false);
            voteButton.setEnabled(false);
            //Синхрон със Сървъра
            try {
                Thread.sleep(1000); // Изчакване за половин сек
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {

            voteErrorText.setText("Моля, изберете кандидат преди да потвърдите гласа.");
        }
    }

    private class SendVoteTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String verCode = VerificationActivity.getVerCode();

            String voteData = "voteResult:"+verCode+":"+selectedCandidateId;
            System.out.println("tova izprashta"+voteData);

            return MyApplication.sendVoteResult(voteData);

        }

        @Override
        protected void onPostExecute(String result) {
            // Обработка на резултата от изпращането на гласа
            System.out.println("Това идва в onPost: " + result);
            String[] parts = result.split(":");
            if (parts[2].equals("1")
                    || parts[2].equals("2")
                    || parts[2].equals("3")) {
                try {
                    Intent intent = new Intent(VoteActivity.this, SuccessVote.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(VoteActivity.this, "error", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(VoteActivity.this, "Грешка в резултата от изпращането на гласа", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, VerificationActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
