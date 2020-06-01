import 'dart:async';
import 'dart:core';
import 'dart:ui';

import 'package:flutter/services.dart';
import 'package:flutter_local_notifications/src/callback_dispatcher/callback_dispatcher.dart';

class BackgroundManager {
  static const MethodChannel _channel =
      MethodChannel('dexterous.com/flutter/background_manager_dart_channel');
  static const MethodChannel _background =
      MethodChannel('dexterous.com/flutter/background_dart_channel');

  // initialize the background manager plugin
  static Future<void> initialize() async {
    final CallbackHandle callback =
        PluginUtilities.getCallbackHandle(callbackDispatcher);
    await _channel.invokeMethod(
        'BackgroundManager.initializeServ', <dynamic>[callback.toRawHandle()]);
  }

  static Future<void> registerCallback(Function callback) async {
    final arg = PluginUtilities.getCallbackHandle(callback).toRawHandle();
    await _channel.invokeMethod(
        'BackgroundManagerPlugin.registerCallback', arg);
  }

  //promote background manager service to a foreground service
  static Future<void> promoteToForeground() async => await _background
      .invokeMethod('BackgroundManagerService.promoteToForeground');

  //demote background manager service to a background servcie
  static Future<void> demoteFromForeground() async => await _background
      .invokeMethod('BackgroundManagerService.demoteToBackground');
}
