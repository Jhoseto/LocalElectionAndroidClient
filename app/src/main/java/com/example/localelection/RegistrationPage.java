package com.example.localelection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationPage extends AppCompatActivity {
    private EditText username ;
    private EditText age;
    private EditText email;
    private EditText password;
    private EditText password2;
    private TextView registrationErrorText;
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
        setContentView(R.layout.activity_registration_page);

        //Подържане на включен екран
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Инициализация на VideoView
        videoView = findViewById(R.id.videoView3);
        // Видео файл за възпроизвеждане
        videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.regpagevideo);
        videoView.setVideoURI(videoUri);
        videoView.start();
        // Слушател за завършване на видеото
        videoView.setOnCompletionListener(mediaPlayer -> {
            videoView.start();
        });
        videoView.start();


        // Инициализация на UI елементи
        username = findViewById(R.id.usernameFeald);
        age = findViewById(R.id.ageField);
        email = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        password2 = findViewById(R.id.passwordField2);
        registrationErrorText = findViewById(R.id.registrationErrorText);

        Button agreeButton = findViewById(R.id.agreeButton);
        agreeButton.setOnClickListener(v -> addRegistration());

    }

    public boolean isValidUsername() {
        String user = username.getText().toString();
        // Проверка дали потребителското име е празно и има поне 3 имена
        if (user.isEmpty() || user.split("\\s+").length < 3) {
            return false;
        }
        // Проверка дали всяко име започва с главна буква
        String[] names = user.split("\\s+");
        for (String name : names) {
            if (!Character.isUpperCase(name.charAt(0))) {
                return false;
            }
        }
        // Проверка дали потребителското име не съдържа цифри
        if (user.matches(".*\\d+.*")) {
            return false;
        }
        return true;
    }

    public boolean isValidAge() {
        String ageText = age.getText().toString();
        if (ageText.isEmpty() || Integer.parseInt(ageText) < 18) {
            return false;
        }
        if (!ageText.matches("\\d+")) {
            return false;
        }
        return true;
    }

    public boolean isValidEmail() {
        String emailText = email.getText().toString().trim();
        if (emailText.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
    }

    public boolean isValidPassword() {
        String passwordText = password.getText().toString();
        String password2Text = password2.getText().toString();
        if (!passwordText.matches(".*[^a-zA-Z0-9].*")) {
            return false;
        } else if (!passwordText.matches(".*[A-Z].*")) {
            return false;
        } else if (passwordText.replaceAll("\\D", "").length() < 2) {
            return false;
        } else if (!password2Text.equals(passwordText)) {
            return false;
        }
        return true;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("newRegistration:%s:%s:%s:%s",
                username.getText().toString(),
                age.getText().toString(),
                email.getText().toString(),
                password.getText().toString());
        }
        
    @SuppressLint("SetTextI18n")
    private void addRegistration() {
        if (!isValidUsername()) {
            registrationErrorText.setText("Грешка: Въведете валидни три имена, " +
                    "като всяко от тях трябва да започва с главна буква.");
        } else if (!isValidAge()) {
            registrationErrorText.setText("Грешка: Въведете валидна възраст над 18 години.");
        } else if (!isValidEmail()) {
            registrationErrorText.setText("Грешка: Въведете валиден имейл адрес.");
        } else if (!isValidPassword()) {
            registrationErrorText.setText("Грешка: Въведете парола, " +
                    "като използвате поне един символ различен от букви и цифри, поне две цифри и поне една главна буква.");
        } else {
            Toast.makeText(this, "Проверка на регистрацията Изчакайте ...", Toast.LENGTH_SHORT).show();
            //  Registration към сървъра
            MyApplication.sendData(toString());

            // лог, който извежда информацията за изпратеното съобщение
            Log.i("Registration", "Sent registration data: " + toString());
            try {
                Thread.sleep(3000); // Изчакване за 3 секунди
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String registrationResultFromServer = MyWebSocketClient.getRegistrationResult();
            System.out.println(registrationResultFromServer);

            if (registrationResultFromServer.equals("-0")){
                registrationErrorText.setText("Грешка: Вече съществува регистриран " +
                        "гласоподавател с този имейл адрес.");
            } else if (registrationResultFromServer.equals("-1")) {

                Intent intent = new Intent(RegistrationPage.this, SuccessRegistration.class);
                startActivity(intent);
                finish();
            }
        }
    }
    public void backToMainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
