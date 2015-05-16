#import "GameAnalyticsPlugin.h"

@implementation GameAnalyticsPlugin

// The plugin must call super init.
- (id) init {
  if (self = [super init]) {
  }
  return self;
}

- (void) initializeWithManifest:(NSDictionary *)manifest appDelegate:(TeaLeafAppDelegate *)appDelegate {
  NSDictionary *ios = [manifest valueForKey:@"ios"];
  NSDictionary *gameAnalyticsResources = [manifest valueForKey:@"gameanalytics"];

  [GameAnalytics configureBuild:[NSString stringWithFormat:@"%@",[manifest valueForKey:@"version"]]];

  [GameAnalytics configureAvailableResourceCurrencies:[gameAnalyticsResources valueForKey;@"currencies"]];
  [GameAnalytics configureAvailableResourceItemTypes:[gameAnalyticsResources valueForKey;@"itemTypes"]];

  [GameAnalytics setEnabledInfoLog:YES];
  [GameAnalytics initializeWithGameKey:[ios valueForKey:@"gameanalyticsGameKey"]
                            gameSecret:[ios valueForKey:@"gameanalyticsSecretKey"]];
}

- (void) setUserInfo:(NSDictionary *) jsonData {
  NSString *gender = [jsonData objectForKey:@"gender"];
  NSNumber *birthYear = [jsonData objectForKey:@"birthYear"];
  NSNumber *facebookId = [jsonData objectForKey:@"facebook_id"];

  if(gender == (id)[NSNull null] || gender.length == 0 ) {
    gender = nil;
  }
  if ([birthYear isKindOfClass:[NSNull class]]) {
    birthYear = nil;
  }
  if(facebookId == (id)[NSNull null] || gender.length == 0 ) {
    facebookId = nil;
  }

  [GameAnalytics setGender: gender];
  [GameAnalytics setBirthYear: birthYear];
  [GameAnalytics setFacebookId: facebookId];
}

- (void) newBusinessEvent:(NSDictionary *) jsonData {
  [GameAnalytics addBusinessEventWithCurrency:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"currency"]]
                                       amount:[[jsonData objectForKey:@"amount"] intValue]
                                     itemType:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"item_type"]]
                                       itemId:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"item_id"]]
                                     cartType:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"cart_type"]]
                                      receipt:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"receipt"]];
}

- (void) newResourceEvent:(NSDictionary *) jsonData {
  [GameAnalytics addResourceEventWithFlowType:[jsonData objectForKey:@"flow_type"]
                                     currency:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"currency"]]
                                       amount:[NSNumber numberWithInteger:[[jsonData objectForKey:@"amount"]intValue]]
                                     itemType:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"item_type"]]
                                       itemId:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"item_id"]]];
}

- (void) newDesignEvent:(NSDictionary *) jsonData {
  [GameAnalytics addDesignEventWithEventId:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"event_id"]]
                                     value:[NSNumber numberWithInteger:[[jsonData objectForKey:@"value"]intValue]]];
}

- (void) newProgressionEvent: (NSDictionary *) jsonData {
 [GameAnalytics addProgressionEventWithProgressionStatus:[jsonData objectForKey:@"status"]
                                   progression01:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"prog_1"]]
                                   progression02:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"prog_2"]]
                                   progression03:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"prog_3"]]
                                   score: [[jsonData objectForKey:@"score"] intvalue]];
}

- (void) newErrorEvent: (NSDictionary *) jsonData {
  [GameAnalytics addErrorEventWithSeverity:[jsonData objectForKey:@"severity"]
                                   message:[NSString stringWithFormat:@"%@", [jsonData objectForKey:@"message"]]]
}

@end
