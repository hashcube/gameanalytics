var debug = true,

  hasNativeEvents = NATIVE && NATIVE.plugins && NATIVE.plugins.sendEvent,

  sendEvent = function (fn, data) {
    if (hasNativeEvents) {
      NATIVE.plugins.sendEvent("GameAnalyticsPlugin", fn, JSON.stringify(data || {}));
    }
  },


  log = function() {
    if(debug) {
      logger.log.apply(logger, arguments);
    }
  },

  GameAnalytics = Class(function () {
    this.init = function () {
      log('GameAnalytics: init');
    };

    this.logFPS = function () {
      log('GameAnalytics: logFPS');
      NATIVE.plugins.sendEvent("GameAnalyticsPlugin", "logFPS");
    };
  });

exports = new GameAnalytics();
