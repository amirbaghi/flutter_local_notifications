package com.dexterous.flutterlocalnotifications.services;

import android.content.Intent;
import android.app.IntentService;
import android.util.Log;

import com.dexterous.flutterlocalnotifications.models.NotificationAction;


import io.flutter.plugin.common.MethodChannel;

public class LocalNotificationsService extends IntentService {
    private static MethodChannel sSharedChannel;

    public static final String LOGGING_TAG = "LocalNotifications";

    public LocalNotificationsService() {
        super("LocalNotificationsService");
    }

    public static MethodChannel getSharedChannel() {
        return sSharedChannel;
    }

    public static void setSharedChannel(MethodChannel channel) {
        if (sSharedChannel != null && sSharedChannel != channel) {
            Log.d(LOGGING_TAG, "sSharedChannel tried to overwrite an existing Registrar");
            return;
        }
        Log.d(LOGGING_TAG, "sSharedChannel set");
        sSharedChannel = channel;
    }

    @Override
    public void onHandleIntent(Intent intent) {
        handleIntent(intent);
    }

    public static boolean handleIntent(Intent intent) {

        if (intent == null) {
            return false;
        }

        return checkAndInvokeCallback(intent);
    }

    private static boolean checkAndInvokeCallback(Intent intent) {
        String callbackName = intent.getStringExtra(NotificationAction.CALLBACK_KEY);
        String payload = intent.getStringExtra(NotificationAction.PAYLOAD_KEY);

        System.out.println(callbackName);
        if (isNullOrEmpty(callbackName)) {
            return false;
        }

        return invokeCallback(callbackName, payload);
    }

    private static boolean invokeCallback(String callbackName, String payload) {
        MethodChannel channel = LocalNotificationsService.getSharedChannel();
        if (channel != null) {
            channel.invokeMethod(callbackName, payload);
            System.out.println("ddddddddddddddddddddddd");
            return true;
        } else {
            return false;
        }
    }

    private static boolean isNullOrEmpty(String callbackName) {
        return callbackName == null || "".equals(callbackName);
    }
}