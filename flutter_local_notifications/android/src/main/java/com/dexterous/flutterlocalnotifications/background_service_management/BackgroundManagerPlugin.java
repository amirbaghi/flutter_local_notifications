package com.dexterous.flutterlocalnotifications.background_service_management;

import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class BackgroundManagerPlugin implements MethodCallHandler {

    private final Context mContext;
    private final Activity mActivity;

    public static final String CALLBACK_DISPATCHER_HANDLE_KEY = "callback_dispatch_handler";
    public static final String CALLBACK_HANDLE_KEY = "callback_handle";
    public static final String SHARED_PREFERENCES_KEY = "plugin_cache";

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(),
                "dexterous.com/flutter/background_manager_dart_channel");
        channel.setMethodCallHandler(new BackgroundManagerPlugin(registrar.context(), registrar.activity()));
    }

    public BackgroundManagerPlugin(Context context, Activity activity) {
        this.mActivity = activity;
        this.mContext = context;
    }



    // private static void addCallbackToCache(Context context, String id, )

    // TODO: Re Register After Reboot should be checked, Probably not needed

    // Initializing the service by getting the handle and saving it as cache for
    // further use
    private static void intializeServ(Context context, ArrayList<Object> args) {
        Long callBackHandle = (Long) args.get(0);
        context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit()
                .putLong(CALLBACK_DISPATCHER_HANDLE_KEY, callBackHandle).apply();
    }


    private static void registerCallback(Context context, Result result, Boolean cache, Long arg){
        Long callbackHandle = arg;

    }

    // TODO: PendingIntent should be handled

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        ArrayList<Object> args = (ArrayList<Object>) call.arguments;
        System.out.println("hey");
        switch (call.method) {
            case "BackgroundManager.initializeServ":
                intializeServ(mContext, args);
                result.success(null);
                break;
            default:
                result.notImplemented();
                break;
        }
    }


}