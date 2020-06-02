package com.dexterous.flutterlocalnotifications.models;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dexterous.flutterlocalnotifications.services.LocalNotificationsService;
<<<<<<< HEAD
<<<<<<< HEAD
import com.dexterous.flutterlocalnotifications.background_service_management.BackgroundManagerBroadcastReceiver;
import com.dexterous.flutterlocalnotifications.FlutterLocalNotificationsPlugin;
=======
import com.dexterous.flutterlocalnotifications.background_service_management.BackgroundManagerPlugin;
import com.dexterous.flutterlocalnotifications.background_service_management.BackgroundManagerBroadcastReceiver;
>>>>>>> added support for background headless dart code execution, modified the logic of the plugin based on the new procedure for running flutter functions
=======
import com.dexterous.flutterlocalnotifications.background_service_management.BackgroundManagerBroadcastReceiver;
import com.dexterous.flutterlocalnotifications.FlutterLocalNotificationsPlugin;
>>>>>>> Integrated the BackgroundManagementPlugin into FlutterLocalNotificationsPlugin and deleted it itself, now headless dart code is executed in the background and working for Android, cleaned up the modules a bit by removing prints and commented out sections, also modified the AndroidManifest.xml and main.dart of example appropriately



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
<<<<<<< HEAD
        Intent actionIntent = new Intent(context, BackgroundManagerBroadcastReceiver.class);
=======
        System.out.println("Start of not launches app");
        Intent actionIntent = new Intent(context, LocalNotificationsService.class);
>>>>>>> added actions for the example app plain notification, also fixed some bugs in the flutter and android side for parsing the actions data
=======
        Intent actionIntent = new Intent(context, BackgroundManagerBroadcastReceiver.class);
>>>>>>> added support for background headless dart code execution, modified the logic of the plugin based on the new procedure for running flutter functions
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
<<<<<<< HEAD
<<<<<<< HEAD
            actionIntent.putExtra(FlutterLocalNotificationsPlugin.CALLBACK_HANDLE_KEY, callbackHandle);
=======
            // actionIntent.putExtra(CALLBACK_KEY, callbackFunctionName);
<<<<<<< HEAD
            actionIntent.putExtra(BackgroundManagerPlugin.CALLBACK_HANDLE_KEY, callbackHandle);
>>>>>>> added support for background headless dart code execution, modified the logic of the plugin based on the new procedure for running flutter functions
=======
=======
>>>>>>> resolved conflicts
            actionIntent.putExtra(FlutterLocalNotificationsPlugin.CALLBACK_HANDLE_KEY, callbackHandle);
>>>>>>> Integrated the BackgroundManagementPlugin into FlutterLocalNotificationsPlugin and deleted it itself, now headless dart code is executed in the background and working for Android, cleaned up the modules a bit by removing prints and commented out sections, also modified the AndroidManifest.xml and main.dart of example appropriately
            actionIntent.putExtra(PAYLOAD_KEY, payload);
        }
    }
}