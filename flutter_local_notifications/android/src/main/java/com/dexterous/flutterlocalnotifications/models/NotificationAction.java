package com.dexterous.flutterlocalnotifications.models;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dexterous.flutterlocalnotifications.services.LocalNotificationsService;


import java.util.Objects;

public class NotificationAction {
    private String callbackFunctionName;
    public String actionText;
    private String payload;
    private boolean launchesApp;
    private static int currentId = 0;
    public static final String CALLBACK_KEY = "callback_key";     
    public static final String PAYLOAD_KEY = "payload_key";

    public NotificationAction(String callbackFunctionName, String actionText, String payload, boolean launchesApp) {
        this.callbackFunctionName = callbackFunctionName;
        this.actionText = actionText;
        this.payload = payload;
        this.launchesApp = launchesApp;
    }

    private boolean isEmptyAction() {
        return "".equals(this.callbackFunctionName)
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
        System.out.println("Start of not launches app");
        Intent actionIntent = new Intent(context, LocalNotificationsService.class);
        addActionsToIntent(actionIntent);
        return PendingIntent.getService(context, currentId++, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
            actionIntent.putExtra(CALLBACK_KEY, callbackFunctionName);
            actionIntent.putExtra(PAYLOAD_KEY, payload);
            System.out.println(CALLBACK_KEY + callbackFunctionName);
            System.out.println(PAYLOAD_KEY + payload);
        }
    }
}