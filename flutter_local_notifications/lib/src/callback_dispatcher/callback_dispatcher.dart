import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';


// Function responsible to handle callbacks from Native
void callbackDispatcher() {
  const MethodChannel _backgroundChannel =
      MethodChannel('dexterous.com/flutter/background_dart_channel');
  WidgetsFlutterBinding.ensureInitialized();

  _backgroundChannel.setMethodCallHandler((MethodCall call) async {
    final List<dynamic> args = call.arguments;
    final Function callback = PluginUtilities.getCallbackFromHandle(
        CallbackHandle.fromRawHandle(args[0]));
    assert(callback != null);
    // Invoking the corresponding function
    callback();
  });

  // Letting the Native part know that the service has been intitialized
  _backgroundChannel.invokeMethod('backgroundServiceInitialized');
}
