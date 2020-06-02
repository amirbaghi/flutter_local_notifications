package com.dexterous.flutterlocalnotifications.background_service_management;

import com.dexterous.flutterlocalnotifications.background_service_management.BackgroundManagerService;


import com.dexterous.flutterlocalnotifications.FlutterLocalNotificationsPlugin;
// import com.dexterous.flutterlocalnotifications.background_service_management.IsolateHolderService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import androidx.core.app.JobIntentService;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry.PluginRegistrantCallback;
import io.flutter.view.FlutterCallbackInformation;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.app.FlutterPluginRegistry;
import io.flutter.view.FlutterRunArguments;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.UUID;

public class BackgroundManagerService extends JobIntentService implements MethodCallHandler {

    private Deque<List<Object>> queue = new ArrayDeque<List<Object>>();
    private Context mContext;
    private MethodChannel mBackgroundChannel;

    private static final String TAG = "BackgroundManagerService";
    private static int JOB_ID = (int)UUID.randomUUID().getMostSignificantBits();
    private static FlutterNativeView sBackgroundFlutterView;
    private static AtomicBoolean sServiceStarted = new AtomicBoolean(false);
    private static PluginRegistrantCallback sPluginRegistrantCallback;

    public static void enqueueWork(Context context, Intent work) {
        JobIntentService.enqueueWork(context, BackgroundManagerService.class, JOB_ID, work);
    }

    public static void setPluginRegristrant(PluginRegistrantCallback callback) {
        sPluginRegistrantCallback = callback;
    }

    private void startBackgroundManagerService(Context context) {
        synchronized (sServiceStarted) {
            mContext = context;
            if (sBackgroundFlutterView == null) {
                Long callbackHandle = context
                        .getSharedPreferences(FlutterLocalNotificationsPlugin.SHARED_PREFERENCES_KEY_BACKGROUND, Context.MODE_PRIVATE)
                        .getLong(FlutterLocalNotificationsPlugin.CALLBACK_DISPATCHER_HANDLE_KEY, 0);
                FlutterCallbackInformation callbackInfo = FlutterCallbackInformation
                        .lookupCallbackInformation(callbackHandle);
                if (callbackHandle == null) {
                    Log.e(TAG, "Fatal: failed to find callback");
                    return;
                }
                Log.i(TAG, "Starting Background Service...");
                sBackgroundFlutterView = new FlutterNativeView(context, true);
                FlutterPluginRegistry resgistry = sBackgroundFlutterView.getPluginRegistry();

                // Registering the Plugin after the engine has been started
                FlutterLocalNotificationsPlugin.registerWith(resgistry.registrarFor("com.dexterous.flutterlocalnotifications.FlutterLocalNotifications"));                

                FlutterRunArguments args = new FlutterRunArguments();
                args.bundlePath = FlutterMain.findAppBundlePath(context);
                args.entrypoint = callbackInfo.callbackName;
                args.libraryPath = callbackInfo.callbackLibraryPath;

                sBackgroundFlutterView.runFromBundle(args);
            }
        }
        mBackgroundChannel = new MethodChannel(sBackgroundFlutterView,
                "dexterous.com/flutter/background_dart_channel");
        mBackgroundChannel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        switch (call.method) {
            case "backgroundServiceInitialized":
                synchronized (sServiceStarted) {
                    while (!queue.isEmpty()) {
                        mBackgroundChannel.invokeMethod("", queue.remove());
                    }
                    sServiceStarted.set(true);
                }
                break;
            default:
                result.notImplemented();
                break;
        }
        result.success(null);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startBackgroundManagerService(this);
    }

    @Override
    public void onHandleWork(Intent intent) {
        Long callbackHandle = intent.getLongExtra(FlutterLocalNotificationsPlugin.CALLBACK_HANDLE_KEY, 0);
        String payload = intent.getStringExtra("payload_key");

        final ArrayList<Object> workList = new ArrayList<>();
        workList.add(callbackHandle);
        workList.add(payload);

        synchronized (sServiceStarted) {
            if (!sServiceStarted.get()) {
                queue.add(workList);
            } else {
                new Handler(mContext.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        mBackgroundChannel.invokeMethod("", workList);
                    }
                });
            }
        }
    }

}