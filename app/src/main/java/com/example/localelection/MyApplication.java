package com.example.localelection;
import android.app.Application;
import android.os.Looper;
import android.os.Handler;

public class MyApplication extends Application {


    private static final String SERVER_URI = "ws://213.91.128.33:2662";
    private static Handler mainHandler;



    @Override
    public void onCreate() {
        super.onCreate();

        MyWebSocketClient.connectWebSocket(SERVER_URI);
        mainHandler = new Handler(Looper.getMainLooper()) {
        };
    }

    public static void sendData(String registration) {
        MyWebSocketClient clientInstance = MyWebSocketClient.getInstance();
        if (clientInstance != null) {

            // Проверка дали сме в главната нишка
            if (Looper.myLooper() == Looper.getMainLooper()) {
                clientInstance.sendData(registration);
            } else {
                // Ако сме във вторична нишка, използваме mainHandler за изпращане на съобщение към главната нишка
                MyWebSocketClient finalClientInstance = clientInstance;
                mainHandler.post(() -> finalClientInstance.sendData(registration));
            }
        } else {

            int maxAttempts = 5; // брой опити
            int currentAttempt = 0;

            while (clientInstance == null && currentAttempt < maxAttempts) {
                // Изчакваме, преди да опитате отново
                try {
                    Thread.sleep(1000); // Изчакваме 2 секунди
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clientInstance = MyWebSocketClient.getInstance();
                currentAttempt++;
            }

            if (clientInstance != null) {
                clientInstance.sendData(registration);
            } else {
            }
        }
    }

    public static String sendLoginCheck(String loginData) {
        MyWebSocketClient clientInstance = MyWebSocketClient.getInstance();
        if (clientInstance != null) {
            clientInstance.sendLoginCheck(loginData);
        } else {
            // Логика за повторен опит за изпращане
            int maxAttempts = 5; // брой опити
            int currentAttempt = 0;

            while (clientInstance == null && currentAttempt < maxAttempts) {
                // Изчакваме, преди да опитате отново
                try {
                    Thread.sleep(1000); // Изчакваме 2 секунди
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                clientInstance = MyWebSocketClient.getInstance();
                currentAttempt++;
            }
            if (clientInstance != null) {
                clientInstance.sendLoginCheck(loginData);
            } else {

            }
        }
        return loginData;
    }

    public static String sendVerificationCheck(String verificationData) {
        MyWebSocketClient clientInstance = MyWebSocketClient.getInstance();
        if (clientInstance != null) {
            clientInstance.sendVerificationCheck(verificationData);
        } else {
            // Логика за повторен опит за изпращане
            int maxAttempts = 5; // брой опити
            int currentAttempt = 0;

            while (clientInstance == null && currentAttempt < maxAttempts) {
                // Изчакваме, преди да опитате отново
                try {
                    Thread.sleep(1000); // Изчакваме 2 секунди
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                clientInstance = MyWebSocketClient.getInstance();
                currentAttempt++;
            }
            if (clientInstance != null) {
                clientInstance.sendVerificationCheck(verificationData);
            } else {

            }
        }
        return verificationData;
    }


    public static String sendVoteResult(String voteResult) {
        MyWebSocketClient clientInstance = MyWebSocketClient.getInstance();
        if (clientInstance != null) {
            clientInstance.sendVoteResult(voteResult);
        } else {
            int maxAttempts = 5;
            int currentAttempt = 0;

            while (clientInstance == null && currentAttempt < maxAttempts) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                clientInstance = MyWebSocketClient.getInstance();
                currentAttempt++;
            }
            if (clientInstance != null) {
                clientInstance.sendVoteResult(voteResult);
            } else {

            }
        }
        return voteResult;
    }
}
