package com.example.localelection;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class MyWebSocketClient extends WebSocketClient {
    private static MyWebSocketClient instance;
    private static OnMessageReceivedListener messageReceivedListener;

    private final Handler handler;

    public interface OnMessageReceivedListener {
        void onMessageReceived(String message);
    }
    private static String registrationResult;
    private static String loginResult;
    private static String verResult;
    private static String voteResult;

    public static String getRegistrationResult() {
        return registrationResult;
    }

    public static void setRegistrationResult(String result) {
        System.out.println("SETRegistrationResult"+result);
        registrationResult = result;
    }

    public static String getVerResult() {
        return verResult;
    }

    public static void setVerResult(String result) {
        System.out.println("SETVerResult"+verResult);
        verResult = result;
    }

    public static String getLoginResult() {
        return loginResult;
    }

    public static void setLoginResult(String result) {
        System.out.println("SETLoginResult"+result);
        loginResult = result;
    }

    public static String getVoteResult() {
        return voteResult;
    }

    public static void setVoteResult(String result) {
        voteResult = result;
    }

    private MyWebSocketClient(URI serverUri, Handler handler) {
        super(serverUri);
        this.handler = handler;
    }

    public static MyWebSocketClient getInstance() {
        return instance;
    }

    public static void connectWebSocket(String serverUri) {
        try {
            instance = new MyWebSocketClient(new URI(serverUri), new Handler(Looper.getMainLooper()));
            instance.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String registration) {
        if (isOpen()) {
            send(registration);
        } else {
            Log.e("WebSocket", "WebSocket connection not open. " +
                    "Unable to send message: " + registration);
        }
    }

    public void sendLoginCheck(String loginCheck) {
        if (isOpen()) {
            send(loginCheck);
        } else {
            Log.e("WebSocket", "WebSocket connection not open. " +
                    "Unable to send message: " + loginCheck);
        }
    }

    public void sendVerificationCheck(String verificationCheck) {
        if (isOpen()) {
            send(verificationCheck);
        } else {
            Log.e("WebSocket", "WebSocket connection not open. " +
                    "Unable to send message: " + verificationCheck);
        }
    }
    public void sendVoteResult(String vote) {
        if (isOpen()) {
            send(vote);
        } else {
            Log.e("WebSocket", "WebSocket connection not open. " +
                    "Unable to send message: " + vote);
        }
    }



    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("WebSocket", "Connected to server");

        send("NEW Device connected!");
    }

    @Override
    public void onMessage(String message) {
        Log.i("WebSocket", "Received message: " + message);

        processReceivedMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i("WebSocket", "Connection closed: " + reason);

    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();

    }

    public String processReceivedMessage(String message) {
        try {
            if (message.startsWith("registrationResult:")) {
                String[] parts = message.split(":");
                if (parts.length == 2) {
                    String result = parts[1];

                    setRegistrationResult(result);

                    return result;
                }
            }
            if (message.startsWith("loginResult:")) {
                String[] parts = message.split(":");
                if (parts.length == 2) {
                    String result = parts[1];

                    setLoginResult(result);

                    return result;
                }
            }
            if (message.startsWith("verResult:")) {
                String[] parts = message.split(":");
                if (parts.length == 2) {
                    String result = parts[1];

                    setVerResult(result);

                    return result;
                }
            }
            if (message.startsWith("voteResult:")) {
                String[] parts = message.split(":");
                if (parts.length == 2) {
                    String voteResult = parts[1];
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("WebSocket", "Error processing received message: " + e.getMessage());
        }
        return null;
    }
}
