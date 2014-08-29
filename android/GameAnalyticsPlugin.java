package com.tealeaf.plugin.plugins;

import com.tealeaf.logger;
import com.tealeaf.plugin.IPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import com.gameanalytics.android.GameAnalytics;
import com.gameanalytics.android.Severity;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class GameAnalyticsPlugin implements IPlugin {
  private Activity _activity;
  private static String LOGID = "{GameAnalytics Native}";
  private static boolean DEBUG = false;

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
    _activity = activity;

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

  public void logFPS(String jsonData) {
    GameAnalytics.logFPS();
    log("logFPS");
  }

  public void setUserInfo(String jsonData) {
    char gender;
    int birthYear;
    int friendCount;

    try {
      JSONObject obj = new JSONObject(jsonData);
      gender = obj.getString("gender").charAt(0);
      birthYear = obj.getInt("birthYear");
      friendCount = obj.getInt("friendCount");
      GameAnalytics.setUserInfo(gender, birthYear, friendCount);
      log("setUserInfo: " + Character.toString(gender) + ", " + Integer.toString(birthYear) + ", " + Integer.toString(friendCount));
    } catch (JSONException e) {
      log("setUserInfo failed: " + e.getMessage());
    }
  }

  public void newBusinessEvent(String jsonData) {
    String item;
    String currency;
    int amount;

    try {
      JSONObject obj = new JSONObject(jsonData);
      item = obj.getString("item");
      currency = obj.getString("currency");
      amount = obj.getInt("amount");
      GameAnalytics.newBusinessEvent(item, currency, amount);
      log("newBusinessEvent: " + item + ", " + currency + ", " + Integer.toString(amount));
    } catch (JSONException e) {
      log("newBusinessEvent failed: " + e.getMessage());
    }

  }

  public void newDesignEvent(String jsonData) {
    String eventId;
    float value;

    try {
      JSONObject obj = new JSONObject(jsonData);
      eventId = obj.getString("eventId");
      value = (float) obj.getDouble("value");
      GameAnalytics.newDesignEvent(eventId, value);
      log("newDesignEvent: " + eventId + ", " + Float.toString(value));
    } catch (JSONException e) {
      log("newDesignEvent failed: " + e.getMessage());
    }
  }

  public void newErrorEvent(String message) {
    //TODO: get error severity level as parameter for the function
    Severity severity_level = GameAnalytics.ERROR_SEVERITY;
    GameAnalytics.newErrorEvent(message, severity_level);
    log("newErrorEvent: " + message);
  }

  public void setNetworkPollInterval(String jsonData) {
    int value = Integer.parseInt(jsonData);

    GameAnalytics.setNetworkPollInterval(value);
    log("setNetworkPollInterval: " + Integer.toString(value));
  }

  public void setSendEventsInterval(String jsonData) {
    int value = Integer.parseInt(jsonData);

    GameAnalytics.setSendEventsInterval(value);
    log("setSendEventsInterval: " + Integer.toString(value));
  }

  public void setSessionTimeOut(String jsonData) {
    int value = Integer.parseInt(jsonData);

    GameAnalytics.setSessionTimeOut(value);
    log("setSessionTimeOut: " + Integer.toString(value));
  }

  public void onResume() {
    GameAnalytics.startSession(_activity);
    log("onResume");
  }

  public void onStart() {
  }

  public void onPause() {
    GameAnalytics.stopSession();
    log("onPause");
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
