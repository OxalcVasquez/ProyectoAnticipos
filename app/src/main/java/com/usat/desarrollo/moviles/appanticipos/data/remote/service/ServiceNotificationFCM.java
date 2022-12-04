package com.usat.desarrollo.moviles.appanticipos.data.remote.service;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.usat.desarrollo.moviles.appanticipos.MainActivity;
import com.usat.desarrollo.moviles.appanticipos.utils.Config;
import com.usat.desarrollo.moviles.appanticipos.utils.NotificationUtils;

public class ServiceNotificationFCM extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCMToken", token);
        Intent intent = new Intent(Config.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getData().size() > 0) {
            Log.d("FCMMessage", message.getData().toString());
            try {
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Intent intent = new Intent(Config.PUSH_NOTIFICATION);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setAction(Config.PUSH_NOTIFICATION);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
