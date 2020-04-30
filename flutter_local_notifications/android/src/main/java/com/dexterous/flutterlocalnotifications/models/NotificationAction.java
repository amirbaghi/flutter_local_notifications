package com.dexterous.flutterlocalnotifications.models;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dexterous.flutterlocalnotifications.services.LocalNotificationsService;
import com.dexterous.flutterlocalnotifications.background_service_management.BackgroundManagerBroadcastReceiver;
import com.dexterous.flutterlocalnotifications.FlutterLocalNotificationsPlugin;



import java.util.Objects;
import java.util.function.Function;

public class NotificationAction {
    // private String callbackFunctionName;
    private Long callbackHandle;
    public String actionText;
    private String payload;
    private boolean launchesApp;
    private static int currentId = 0;
    public static final String CALLBACK_KEY = "callback_key";     
    public static final String PAYLOAD_KEY = "payload_key";

    public NotificationAction(Long callback, String actionText, String payload, boolean launchesApp) {
        // this.callbackFunctionName = callbackFunctionName;
        this.callbackHandle = callback;
        this.actionText = actionText;
        this.payload = payload;
        this.launchesApp = launchesApp;
    }

    private boolean isEmptyAction() {
        return this.callbackHandle == null
                && "".equals(this.actionText)
                && "".equals(this.payload);
    }

    public PendingIntent getIntent(Context context) {
        System.out.println("get intent");
        return launchesApp
                ? getIntentForLaunchesApp(context)
                : getIntentNotLaunchesApp(context);
    }

    private PendingIntent getIntentNotLaunchesApp(Context context) {
<<<<<<< HEAD
        Intent actionIntent = new Intent(context, BackgroundManagerBroadcastReceiver.class);
=======
        System.out.println("Start of not launches app");
        Intent actionIntent = new Intent(context, LocalNotificationsService.class);
>>>>>>> added actions for the example app plain notification, also fixed some bugs in the flutter and android side for parsing the actions data
        addActionsToIntent(actionIntent);
        return PendingIntent.getBroadcast(context, currentId++, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getIntentForLaunchesApp(Context context) {
        Intent actionIntent = context
                .getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());

        addActionsToIntent(actionIntent);
        return PendingIntent.getActivity(context, currentId++, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addActionsToIntent(Intent actionIntent) {
        System.out.println("addActionsToIntent");
        if (!isEmptyAction() && actionIntent != null) {
            actionIntent.putExtra(FlutterLocalNotificationsPlugin.CALLBACK_HANDLE_KEY, callbackHandle);
            actionIntent.putExtra(PAYLOAD_KEY, payload);
            System.out.println(CALLBACK_KEY + callbackFunctionName);
            System.out.println(PAYLOAD_KEY + payload);
        }
    }
}