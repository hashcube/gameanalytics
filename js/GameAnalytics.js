var debug = true,

  hasNativeEvents = NATIVE && NATIVE.plugins && NATIVE.plugins.sendEvent,

  sendEvent = function (fn, data) {
    if (hasNativeEvents) {
      NATIVE.plugins.sendEvent("GameAnalyticsPlugin", fn, JSON.stringify(data || {}));
    }
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
      NATIVE.plugins.sendEvent("GameAnalyticsPlugin", "logFPS");
    };
  });

exports = new GameAnalytics();
