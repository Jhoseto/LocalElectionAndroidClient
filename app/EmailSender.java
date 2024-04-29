package com.example.localelection;

import android.os.AsyncTask;
import android.util.Log;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "EmailSender";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String USERNAME = "konstantinse38@gmail.com"; // Пътеката към вашия имейл
    private static final String PASSWORD = "Piichai2626"; // Пътеката към вашия имейл парола

    private String recipientEmail;
    private String code;

    public EmailSender(String recipientEmail, String code) {
        this.recipientEmail = recipientEmail;
        this.code = code;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            // Настройки за изпращане на имейл чрез Gmail SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            //отключва сертификата само за проба води до несигурност
            props.put("mail.smtp.ssl.trust", "*");

            // Инициализация на сесията с потребителско име и парола
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            // Създаване на съобщението
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Local Election 2023 Регистрационен код");
            message.setText("Вие направихте успешна регистрация в приложението гласуване през вашето мобилно устройство!\n" +
                    "Можете да влезнете в системата, използвайки вашата електронна поща и парола.\n" +
                    "Въведете вашия верификационен код в полето, след което ще можете да упражните вашето право на глас!\n" +
                    "Вашият регистрационен код е: " + code);

            // Изпращане на съобщението
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            Log.e(TAG, "Грешка при изпращането на имейла.", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            Log.i(TAG, "Имейлът с кода беше изпратен успешно.");
        } else {
            Log.e(TAG, "Грешка при изпращането на имейла.");
        }
    }
}
