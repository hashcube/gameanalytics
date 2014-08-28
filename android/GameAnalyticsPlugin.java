package com.tealeaf.plugin.plugins;

import com.tealeaf.logger;
import com.tealeaf.plugin.IPlugin;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

import com.gameanalytics.android.GameAnalytics;
import com.gameanalytics.android.Severity;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class GameAnalyticsPlugin implements IPlugin {
  private static String LOGID = "{GameAnalytics Native}";
  private static boolean DEBUG = true;

  public GameAnalyticsPlugin() {
  }

  public void onCreateApplication(Context applicationContext) {
  }

  private void log(String msg) {
    if (DEBUG) {
      logger.log(LOGID, msg);
    }
  };

  public void onCreate(Activity activity, Bundle savedInstanceState) {
    PackageManager manager = activity.getPackageManager();
    String gameKey = "";
    String secretKey = "";

    try {
      Bundle meta = manager.getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA).metaData;
      if (meta != null) {
        gameKey = meta.getString("gameanalyticsGameKey");
        secretKey = meta.getString("gameanalyticsSecretKey");
      }
    } catch (Exception e) {
      android.util.Log.d("EXCEPTION", "" + e.getMessage());
    }

    if (DEBUG) {
      GameAnalytics.setDebugLogLevel(GameAnalytics.VERBOSE);
    }

    // Set-up game analytics
    GameAnalytics.initialise(activity, secretKey, gameKey);

    // Turn on automatic logging of unhandled exceptions for main/GUI thread
    GameAnalytics.logUnhandledExceptions();
  }

  public void logFPS(String dummy) {
    log("logFPS");
    GameAnalytics.logFPS();
  }

  public void newBusinessEvent(String item, String currency, int amount) {
    log("newBusinessEvent");
    GameAnalytics.newBusinessEvent(item, currency, amount);
  }

  public void newDesignEvent(String eventId, float value) {
    log("newDesignEvent");
    GameAnalytics.newDesignEvent(eventId, value);
  }

  public void newErrorEvent(String message, Severity severity_level) {
    log("newErrorEvent");
    GameAnalytics.newErrorEvent(message, severity_level);
  }

  public void setNetworkPollInterval(int value) {
    log("setNetworkPollInterval");
    GameAnalytics.setNetworkPollInterval(value);
  }

  public void setSendEventsInterval(int value) {
    log("setSendEventsInterval");
    GameAnalytics.setSendEventsInterval(value);
  }

  public void setSessionTimeOut(int value) {
    log("setSessionTimeOut");
    GameAnalytics.setSessionTimeOut(value);
  }

  public void onResume() {
    log("onResume");
    GameAnalytics.startSession(_activity);
  }

  public void onStart() {
  }

  public void onPause() {
    log("onPause");
    GameAnalytics.stopSession();
  }

  public void onStop() {
  }

  public void onDestroy() {
  }

  public void onNewIntent(Intent intent) {
  }

  public void setInstallReferrer(String referrer) {
  }

  public void onActivityResult(Integer request, Integer result, Intent data) {
  }

  public boolean consumeOnBackPressed() {
    return true;
  }

  public void onBackPressed() {
  }
}
