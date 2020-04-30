package com.dexterous.flutterlocalnotifications.models;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dexterous.flutterlocalnotifications.services.LocalNotificationsService;
<<<<<<< HEAD
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
=======


import java.util.Objects;

public class NotificationAction {
    private String callbackFunctionName;
>>>>>>> integrated notification action models and services for android, also modified the corresponding flutter client side, as well as implementing the needed methods
    public String actionText;
    private String payload;
    private boolean launchesApp;
    private static int currentId = 0;
    public static final String CALLBACK_KEY = "callback_key";     
    public static final String PAYLOAD_KEY = "payload_key";

<<<<<<< HEAD
    public NotificationAction(Long callback, String actionText, String payload, boolean launchesApp) {
        // this.callbackFunctionName = callbackFunctionName;
        this.callbackHandle = callback;
=======
    public NotificationAction(String callbackFunctionName, String actionText, String payload, boolean launchesApp) {
        this.callbackFunctionName = callbackFunctionName;
>>>>>>> integrated notification action models and services for android, also modified the corresponding flutter client side, as well as implementing the needed methods
        this.actionText = actionText;
        this.payload = payload;
        this.launchesApp = launchesApp;
    }

    private boolean isEmptyAction() {
<<<<<<< HEAD
        return this.callbackHandle == null
=======
        return "".equals(this.callbackFunctionName)
>>>>>>> integrated notification action models and services for android, also modified the corresponding flutter client side, as well as implementing the needed methods
                && "".equals(this.actionText)
                && "".equals(this.payload);
    }

    public PendingIntent getIntent(Context context) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        System.out.println("get intent");
=======
>>>>>>> integrated notification action models and services for android, also modified the corresponding flutter client side, as well as implementing the needed methods
=======
        System.out.println("get intent");
>>>>>>> added actions for the example app plain notification, also fixed some bugs in the flutter and android side for parsing the actions data
=======
>>>>>>> added some print statements
=======
        System.out.println("get intent");
>>>>>>> added actions for the example app plain notification, also fixed some bugs in the flutter and android side for parsing the actions data
        return launchesApp
                ? getIntentForLaunchesApp(context)
                : getIntentNotLaunchesApp(context);
    }

    private PendingIntent getIntentNotLaunchesApp(Context context) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
=======
        System.out.println("Start of not launches app");
>>>>>>> added actions for the example app plain notification, also fixed some bugs in the flutter and android side for parsing the actions data
=======
>>>>>>> added some print statements
=======
        System.out.println("Start of not launches app");
>>>>>>> added actions for the example app plain notification, also fixed some bugs in the flutter and android side for parsing the actions data
        Intent actionIntent = new Intent(context, LocalNotificationsService.class);
        addActionsToIntent(actionIntent);
        return PendingIntent.getService(context, currentId++, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
>>>>>>> integrated notification action models and services for android, also modified the corresponding flutter client side, as well as implementing the needed methods
    }

    private PendingIntent getIntentForLaunchesApp(Context context) {
        Intent actionIntent = context
                .getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());

        addActionsToIntent(actionIntent);
        return PendingIntent.getActivity(context, currentId++, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addActionsToIntent(Intent actionIntent) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
=======
        System.out.println("addActionsToIntent");
>>>>>>> added actions for the example app plain notification, also fixed some bugs in the flutter and android side for parsing the actions data
=======
>>>>>>> added some print statements
=======
        System.out.println("addActionsToIntent");
>>>>>>> added actions for the example app plain notification, also fixed some bugs in the flutter and android side for parsing the actions data
        if (!isEmptyAction() && actionIntent != null) {
            actionIntent.putExtra(CALLBACK_KEY, callbackFunctionName);
>>>>>>> integrated notification action models and services for android, also modified the corresponding flutter client side, as well as implementing the needed methods
            actionIntent.putExtra(PAYLOAD_KEY, payload);
            System.out.println(CALLBACK_KEY + callbackFunctionName);
            System.out.println(PAYLOAD_KEY + payload);
        }
    }
}