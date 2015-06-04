#import "GameAnalyticsPlugin.h"

#define NULL_CHECK(x) (x) == (id)[NSNull null] ? nil : x

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
    // We should do this before initilisation, so currencies & itemTypes should be declared in manifest.json
    [GameAnalytics configureAvailableResourceCurrencies:(NSArray*)[gameAnalyticsResources valueForKey:@"currencies"]];
    [GameAnalytics configureAvailableResourceItemTypes:(NSArray*)[gameAnalyticsResources valueForKey:@"itemTypes"]];
    [GameAnalytics setEnabledInfoLog:YES];
    [GameAnalytics setEnabledVerboseLog:YES];
    [GameAnalytics initializeWithGameKey:[ios valueForKey:@"gameanalyticsGameKey"]
                            gameSecret:[ios valueForKey:@"gameanalyticsSecretKey"]];
}

- (void) setUserInfo:(NSDictionary *) jsonData {
    [GameAnalytics setGender:[NSString stringWithFormat:@"%@", NULL_CHECK([jsonData objectForKey: @"gender"])]];
    [GameAnalytics setFacebookId: [NSString stringWithFormat:@"%@", NULL_CHECK([jsonData objectForKey: @"facebook_id"])]];
    [GameAnalytics setBirthYear: [NULL_CHECK([jsonData objectForKey: @"gender"]) intValue]];
}

- (void) newBusinessEvent:(NSDictionary *) jsonData {
    [GameAnalytics addBusinessEventWithCurrency:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"currency"])]
                                         amount:[NULL_CHECK([jsonData objectForKey:@"amount"]) intValue]
                                       itemType:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"item_type"])]
                                         itemId:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"item_id"])]
                                       cartType:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"cart_type"])]
                                        receipt:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"receipt"])]];
}

- (void) newResourceEvent:(NSDictionary *) jsonData {
    [GameAnalytics addResourceEventWithFlowType:(GAResourceFlowType)[NULL_CHECK([jsonData objectForKey:@"flow_type"]) intValue]
                                       currency:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"currency"])]
                                         amount:[NSNumber numberWithInteger:[[jsonData objectForKey:@"amount"]intValue]]
                                       itemType:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"item_type"])]
                                         itemId:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"item_id"])]];
}

- (void) newDesignEvent:(NSDictionary *) jsonData {
    [GameAnalytics addDesignEventWithEventId:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"event_id"])]
                                       value:[NSNumber numberWithInteger:[NULL_CHECK([jsonData objectForKey:@"value"]) intValue]]];
}

- (void) newProgressionEvent: (NSDictionary *) jsonData {
    [GameAnalytics addProgressionEventWithProgressionStatus:(GAProgressionStatus)[NULL_CHECK([jsonData objectForKey:@"status"])intValue]
                                              progression01:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"prog_1"])]
                                              progression02:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"prog_2"])]
                                              progression03:[NSString stringWithFormat:@"%@",NULL_CHECK([jsonData objectForKey:@"prog_3"])]
                                                      score:[NULL_CHECK([jsonData objectForKey:@"score"]) intValue]];
}

- (void) newErrorEvent: (NSDictionary *) jsonData {
    [GameAnalytics addErrorEventWithSeverity:(GAErrorSeverity)[NULL_CHECK([jsonData objectForKey:@"severity"]) intValue]
                                     message:[NSString stringWithFormat:@"%@", NULL_CHECK([jsonData objectForKey:@"message"])]];
}

@end
