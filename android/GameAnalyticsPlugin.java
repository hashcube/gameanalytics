package com.tealeaf.plugin.plugins;

import com.tealeaf.logger;
import com.tealeaf.plugin.IPlugin;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.gameanalytics.sdk.*;
import com.gameanalytics.sdk.GAErrorSeverity;
import com.gameanalytics.sdk.GAProgressionStatus;
import com.gameanalytics.sdk.GAResourceFlowType;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
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
    String version = "";
    String resources;
    JSONObject resourcesData;
    JSONArray resourceCurrencies;
    JSONArray resourceItemTypes;
    String[] currencies;
    String[] itemTypes;

    _activity = activity;

    if (DEBUG) {
      GameAnalytics.setEnabledInfoLog(true);
      GameAnalytics.setEnabledVerboseLog(true);
    }
    try {
      Bundle meta = manager.getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA).metaData;
      if (meta != null) {
        gameKey = meta.getString("gameanalyticsGameKey");
        secretKey = meta.getString("gameanalyticsSecretKey");
        resources = meta.getString("gameanalytics");
        resourcesData = new JSONObject(resources);
        resourceCurrencies = resourcesData.getJSONArray("currencies");
        resourceItemTypes = resourcesData.getJSONArray("itemTypes");

        // Configure available virtual currencies and item types
        currencies = new String[resourceCurrencies.length()];
        for (int i = 0 ; i < resourceCurrencies.length(); i++) {
          try {
            String obj = resourceCurrencies.getString(i);
            currencies[i] = obj;
          } catch (JSONException e) {
           log("getting Currencies failed: " + e.getMessage());
          }
        };

        itemTypes = new String[resourceItemTypes .length()];
        for (int i = 0 ; i < resourceItemTypes.length(); i++) {
          try {
            String obj = resourceItemTypes.getString(i);
            itemTypes[i] = obj;
          } catch (JSONException e) {
           log("getting itemTypes failed: " + e.getMessage());
          }
        };
        GameAnalytics.configureAvailableResourceCurrencies(currencies);
        GameAnalytics.configureAvailableResourceItemTypes(itemTypes);
      }
    } catch (Exception e) {
        log(e.getMessage());
    }


    // Set-up game analytics
    GameAnalytics.initializeWithGameKey(activity, gameKey, secretKey);
  }

  public void setUserInfo(String jsonData) {
    Integer gender = null;
    Integer birthYear = null;
    String facebookId = null;

    try {
      JSONObject obj = new JSONObject(jsonData);

      if (!obj.isNull("gender")) {
          gender = obj.getInt("gender");
      }
      if (!obj.isNull("facebook_id")) {
          facebookId = obj.getString("facebook_id");
      }
      if (!obj.isNull("birth_year")) {
          birthYear = obj.getInt("birth_year");
      }

      GameAnalytics.setGender(gender);
      GameAnalytics.setBirthYear(birthYear);
      GameAnalytics.setFacebookId(facebookId);
    } catch (JSONException e) {
      log("setUserInfo failed: " + e.getMessage());
    }
  }

  public void newBusinessEvent(String jsonData) {
    String currency = "USD";
    Integer amount = 0;
    String itemType = "";
    String itemId = "";
    String cartType = "";
    String reciept = "";
    String signature = null;

    try {
      JSONObject obj = new JSONObject(jsonData);
      if(!obj.isNull("currency")) {
        currency = obj.getString("currency");
      }
      amount = obj.getInt("amount");
      itemType = obj.getString("item_type");
      itemId = obj.getString("item_id");
      cartType = obj.getString("cart_type");
      reciept = obj.getString("reciept");
      signature = obj.optString("signature");

      GameAnalytics.addBusinessEventWithCurrency(currency, amount, itemType, itemId, cartType, reciept, "google_play", signature);
    } catch (JSONException e) {
      log("newBusinessEvent failed: " + e.getMessage());
    }
  }

  public void newResourceEvent(String jsonData) {
    int flowType = 0;
    String currency = "cash";
    float amount = 0;
    String itemType = "";
    String itemId = "";

    try {
      JSONObject obj = new JSONObject(jsonData);
      if (!obj.isNull("currency")) {
        currency = obj.getString("currency");
      }
      if (!obj.isNull("amount")) {
        amount = Float.parseFloat(obj.getString("amount"));
      }
      itemType = obj.getString("item_type");
      itemId = obj.getString("item_id");
      if (!obj.isNull("flow_type")) {
        flowType = obj.optInt("flow_type");
      }

      GameAnalytics.addResourceEventWithFlowType(GAResourceFlowType.valueOf(flowType), currency, amount, itemType, itemId);
    } catch (JSONException e) {
      log("newResourceEvent failed: " + e.getMessage());
    }
  }

  public void newDesignEvent(String jsonData) {
    String eventId = "";
    double value = 0;

    try {
      JSONObject obj = new JSONObject(jsonData);
      eventId = obj.optString("event_id");
      if (!obj.isNull("value")) {
        value = obj.optDouble("value");
      }
      GameAnalytics.addDesignEventWithEventId(eventId, value);
    } catch (JSONException e) {
      log("newDesignEvent failed: " + e.getMessage());
    }
  }

  public void newProgressionEvent(String jsonData) {
    int flowType = 0;
    String prog1 = "";
    String prog2 = "";
    String prog3 = "";
    int score = 0;

    try {
      JSONObject obj = new JSONObject(jsonData);
      prog1 = obj.getString("prog_1");
      prog2 = obj.getString("prog_2");
      prog3 = obj.getString("prog_3");
      if (!obj.isNull("score")) {
        score = obj.getInt("score");
      }
      if (!obj.isNull("status")) {
        flowType = obj.getInt("status");
      }
      GameAnalytics.addProgressionEventWithProgressionStatus(GAProgressionStatus.valueOf(flowType), prog1, prog2, prog3, score);
    } catch (JSONException e) {
      log("newProgressionEvent failed: " + e.getMessage());
    }
  }

  public void newErrorEvent(String jsonData) {
    int severity = 0;
    String message = "";

    try {
      JSONObject obj = new JSONObject(jsonData);
      if (!obj.isNull("severity")) {
        severity = obj.getInt("severity");
      }
      message = obj.getString("message");
      GameAnalytics.addErrorEventWithSeverity(GAErrorSeverity.valueOf(severity), message);
    } catch (JSONException e) {
      log("newErrorEvent failed: " + e.getMessage());
    }
  }

  public void onResume() {
  }

  public void onStart() {
  }

  public void onPause() {
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
