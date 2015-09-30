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
    StringVector currencies = new StringVector();
    StringVector itemTypes = new StringVector();

    _activity = activity;

    try {
      Bundle meta = manager.getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA).metaData;
      if (meta != null) {
        gameKey = meta.getString("gameanalyticsGameKey");
        secretKey = meta.getString("gameanalyticsSecretKey");
        resources = meta.getString("gameanalyticsResources");
        resourcesData = new JSONObject(resources);
        resourceCurrencies = resourcesData.getJSONArray("currencies");
        resourceItemTypes = resourcesData.getJSONArray("itemTypes");

        // Configure available virtual currencies and item types
        for (int i = 0 ; i < resourceCurrencies.length(); i++) {
          try {
            String obj = resourceCurrencies.getString(i);
            currencies.add(obj);
          } catch (JSONException e) {
           log("getting Currencies failed: " + e.getMessage());
          }
        };

        for (int i = 0 ; i < resourceItemTypes.length(); i++) {
          try {
            String obj = resourceItemTypes.getString(i);
            itemTypes.add(obj);
          } catch (JSONException e) {
           log("getting itemTypes failed: " + e.getMessage());
          }
        };
      }
    } catch (Exception e) {
        log(e.getMessage());
    }

    if (DEBUG) {
      GameAnalytics.setEnabledInfoLog(true);
      GameAnalytics.setEnabledVerboseLog(true);
    }

    GameAnalytics.configureAvailableResourceCurrencies(currencies);
    GameAnalytics.configureAvailableResourceItemTypes(itemTypes);

    // Set-up game analytics
    GameAnalytics.initializeWithGameKey(activity, gameKey, secretKey);
  }

  public void setUserInfo(String jsonData) {
    String gender = null;
    Integer birthYear = null;
    String facebookId = null;

    try {
      JSONObject obj = new JSONObject(jsonData);

      if (!obj.isNull("gender")) {
          gender = obj.getString("gender");
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
    String currency;
    int amount;
    String itemType;
    String itemId;
    String cartType;
    String reciept;

    try {
      JSONObject obj = new JSONObject(jsonData);
      currency = obj.getString("currency");
      amount = obj.getInt("amount");
      itemType = obj.getString("item_type");
      itemId = obj.getString("item_id");
      cartType = obj.getString("cart_type");
      reciept = obj.getString("reciept");

      GameAnalytics.addBusinessEventWithCurrency(currency, amount, itemType, itemId, cartType, reciept, "google_play");
    } catch (JSONException e) {
      log("newBusinessEvent failed: " + e.getMessage());
    }
  }

  public void newResourceEvent(String jsonData) {
    int flowType;
    String currency;
    float amount;
    String itemType;
    String itemId;

    try {
      JSONObject obj = new JSONObject(jsonData);
      currency = obj.getString("currency");
      amount = (float) obj.getDouble("amount");
      itemType = obj.getString("item_type");
      itemId = obj.getString("item_id");
      flowType = obj.getInt("flow_type");

      GameAnalytics.addResourceEventWithFlowType(GAResourceFlowType.swigToEnum(flowType), currency, amount, itemType, itemId);
    } catch (JSONException e) {
      log("newResourceEvent failed: " + e.getMessage());
    }
  }

  public void newDesignEvent(String jsonData) {
    String eventId = "";
    double value = 0;

    try {
      JSONObject obj = new JSONObject(jsonData);
      eventId = obj.getString("event_id");
      value = obj.getDouble("value");
      GameAnalytics.addDesignEventWithEventId(eventId, value);
    } catch (JSONException e) {
      log("newDesignEvent failed: " + e.getMessage());
    }
  }

  public void newProgressionEvent(String jsonData) {
    int flowType;
    String prog1;
    String prog2;
    String prog3;
    int score;

    try {
      JSONObject obj = new JSONObject(jsonData);
      prog1 = obj.getString("prog_1");
      prog2 = obj.getString("prog_2");
      prog3 = obj.getString("prog_3");
      score = obj.getInt("score");
      flowType = obj.getInt("status");
      GameAnalytics.addProgressionEventWithProgressionStatus(GAProgressionStatus.swigToEnum(flowType), prog1, prog2, prog3, score);
    } catch (JSONException e) {
      log("newProgressionEvent failed: " + e.getMessage());
    }
  }

  public void newErrorEvent(String jsonData) {
    int severity;
    String message;

    try {
      JSONObject obj = new JSONObject(jsonData);
      severity = obj.getInt("severity");
      message = obj.getString("message");
      GameAnalytics.addErrorEventWithSeverity(GAErrorSeverity.swigToEnum(severity), message);
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
