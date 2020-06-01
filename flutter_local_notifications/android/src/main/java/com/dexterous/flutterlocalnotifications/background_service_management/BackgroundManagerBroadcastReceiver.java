package com.dexterous.flutterlocalnotifications.background_service_management;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import io.flutter.view.FlutterMain;


public class BackgroundManagerBroadcastReceiver extends BroadcastReceiver {

    private static String TAG = "BackgroundManagerBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent){
        FlutterMain.ensureInitializationComplete(context, null);
        BackgroundManagerService.enqueueWork(context, intent);
    }
}