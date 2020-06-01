package com.dexterous.flutterlocalnotifications.background_service_management;

import com.dexterous.flutterlocalnotifications.background_service_management.BackgroundManagerPlugin;
import com.dexterous.flutterlocalnotifications.background_service_management.BackgroundManagerService;
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

    private Deque<List<Long>> queue = new ArrayDeque<List<Long>>();
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
                        .getSharedPreferences(BackgroundManagerPlugin.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
                        .getLong(BackgroundManagerPlugin.CALLBACK_DISPATCHER_HANDLE_KEY, 0);
                FlutterCallbackInformation callbackInfo = FlutterCallbackInformation
                        .lookupCallbackInformation(callbackHandle);
                if (callbackHandle == null) {
                    Log.e(TAG, "Fatal: failed to find callback");
                    return;
                }
                Log.i(TAG, "Starting Background Service...");
                sBackgroundFlutterView = new FlutterNativeView(context, true);
                FlutterPluginRegistry resgistry = sBackgroundFlutterView.getPluginRegistry();
                sPluginRegistrantCallback.registerWith(resgistry);

                FlutterRunArguments args = new FlutterRunArguments();
                args.bundlePath = FlutterMain.findAppBundlePath(context);
                args.entrypoint = callbackInfo.callbackName;
                args.libraryPath = callbackInfo.callbackLibraryPath;

                sBackgroundFlutterView.runFromBundle(args);
                //TODO: Add Isolate holder later
                // IsolateHolderService.setBackgroundFlutterView(sBackgroundFlutterView);
            }
        }
        mBackgroundChannel = new MethodChannel(sBackgroundFlutterView,
                "dexterous.com/flutter/background_manager_dart_channel");
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
            // case "BackgroundManagerService.promoteToForeground":
            //     mContext.startForegroundService(Intent(mContext, IsolateHolderService.class));
            //     break;
            // case "BackgroundManagerService.demoteToBackground":
            //     Intent intent = Intent(mContext, IsolateHolderService.java);
            //     intent.setAction(IsolateHolderService.ACTION_SHUTDOWN);
            //     mContext.startForegroundService(intent);
            //     break;
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
        Long callbackHandle = intent.getLongExtra(BackgroundManagerPlugin.CALLBACK_HANDLE_KEY, 0);

        final ArrayList<Long> workList = new ArrayList<>();
        workList.add(callbackHandle);

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