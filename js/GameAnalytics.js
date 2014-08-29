var debug = false,

  sendEvent = function (fn, data) {
    NATIVE.plugins.sendEvent("GameAnalyticsPlugin", fn, JSON.stringify(data || {}));
  },

  log = function() {
    if(debug) {
      var arg = Array.prototype.slice.call(arguments);

      arg.unshift("GameAnalytics - JS: ");
      logger.log.apply(logger, arg);
    }
  },

  GameAnalytics = Class(function () {
    this.init = function () {
      log('init');
    };

    this.logFPS = function () {
      log('logFPS');
      sendEvent("logFPS");
    };

    this.setUserInfo = function (gender, birth_year, friend_count) {
      log('setUserInfo', gender, birth_year, friend_count);
      sendEvent("setUserInfo", {
        gender: gender,
        birthYear: birthYear,
        friendCount: friendCount
      });
    };

    this.newBusinessEvent = function(item, currency, amount) {
      log('newBusinessEvent', item, currency, amount);
      sendEvent("newBusinessEvent", {
        item: item,
        currency: currency,
        amount: amount
      });
    };

    this.newDesignEvent = function(id, value) {
      log("newDesignEvent", id, value);
      sendEvent("newDesignEvent", {
        eventId: id,
        value: value
      });
    };

    this.newErrorEvent = function(message) {
      log("newErrorEvent", message);
      sendEvent("newErrorEvent", message);
    };

    this.setNetworkPollInterval = function(value) {
      log("setNetworkPollInterval", value);
      sendEvent("setNetworkPollInterval", value);
    };

    this.setSendEventsInterval = function(value) {
      log("setSendEventsInterval", value);
      sendEvent("setSendEventsInterval", value);
    };

    this.setSessionTimeOut = function(value) {
      log("setSessionTimeOut", value);
      sendEvent("setSessionTimeOut", value);
    };
  });

exports = new GameAnalytics();
