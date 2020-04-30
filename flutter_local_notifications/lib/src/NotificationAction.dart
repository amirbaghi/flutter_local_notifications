
import 'package:flutter/foundation.dart';

class NotificationAction {
  final Function(String) callback;
  final String actionText;
  final String payload;
  final bool launchesApp;
  final String callbackName; 
  const NotificationAction({
    @required this.actionText,
    @required this.callback,
    @required this.payload,
    this.launchesApp = true,
    this.callbackName
  });

  static const NotificationAction DEFAULT = const NotificationAction(
      actionText: '',
      callback: null,
      payload: ''
  );

  Map toMapForPlatformChannel() {
    return {
      'callback' : actionText,
      'actionText': actionText,
      'payload': payload,
      'launchesApp': launchesApp,
    };
  }

  static String getCallbackNameFromAction(NotificationAction action) {
    return action.callbackName ?? _nameOfFunction(action.callback);
  }

  static String _nameOfFunction(Function(String) callback) {
    if (callback == null) {
      return '';
    }

    final String longName = callback.toString();
    final int functionIndex = longName.indexOf('Function');
    if (functionIndex == -1) return null;
    final int openQuote = longName.indexOf("'", functionIndex + 1);
    if (openQuote == -1) return null;
    final int closeQuote = longName.indexOf("'", openQuote + 1);
    if (closeQuote == -1) return null;
    return longName.substring(openQuote + 1, closeQuote);
  }
}