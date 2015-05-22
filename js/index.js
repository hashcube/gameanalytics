/* global Class, exports:true, NATIVE, logger */

var debug = false,

  sendEvent = function (fn, data) {
    NATIVE.plugins.sendEvent('GameAnalyticsPlugin', fn,
      JSON.stringify(data || {}));
  },

  log = function () {
    if (debug) {
      var arg = Array.prototype.slice.call(arguments);

      arg.unshift('GameAnalytics - JS: ');
      logger.log.apply(logger, arg);
    }
  },

  GameAnalytics = Class(function () {
    this.init = function () {
      log('init');
    };

    this.setUserInfo = function (gender, birth_year, facebook_id) {
      log('setUserInfo', gender, birth_year, facebook_id);
      sendEvent('setUserInfo', {
        gender: gender,
        birthYear: birth_year,
        facebook_id: facebook_id
      });
    };

    this.newBusinessEvent = function (currency, amount, item_type,
      item_id, cart_type, reciept) {
      log('newBusinessEvent', currency, amount, item_type,
      item_id, cart_type, reciept);
      sendEvent('newBusinessEvent', {
        currency: currency,
        amount: amount,
        item_type: item_type,
        item_id: item_id,
        cart_type: cart_type,
        reciept: reciept
      });
    };

    this.newResourceEvent = function (flow, currency, amount,
      item_type, item_id) {
      log('newResourceEvent', flow, currency, amount, item_type, item_id);
      sendEvent('newResourceEvent', {
        flow_type: flow,
        currency: currency,
        amount: amount,
        item_type: item_type,
        item_id: item_id
      });
    };

    this.newDesignEvent = function (id, value) {
      log('newDesignEvent', id, value);
      sendEvent('newDesignEvent', {
        eventId: id,
        value: value
      });
    };

    this.newProgressionEvent = function (status, prog_1, prog_2, prog_3,
      score) {
      log('newProgressionEvent', status, prog_1, prog_2, prog_3, score);
      sendEvent('newProgressionEvent', {
        status: status,
        prog_1: prog_1,
        prog_2: prog_2,
        prog_3: prog_3,
        score: score
      });
    };

    this.newErrorEvent = function (severity, message) {
      log('newErrorEvent', message);
      sendEvent('newErrorEvent', {
        severity: severity,
        message: message
      });
    };
  });

exports = new GameAnalytics();
