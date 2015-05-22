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
    // We should do this before initilisation, so currencies & itemTypes should be declared in manifest.json
    [GameAnalytics configureAvailableResourceCurrencies:(NSArray*)[gameAnalyticsResources valueForKey:@"currencies"]];
    [GameAnalytics configureAvailableResourceItemTypes:(NSArray*)[gameAnalyticsResources valueForKey:@"itemTypes"]];
    [GameAnalytics setEnabledInfoLog:YES];
    [GameAnalytics initializeWithGameKey:[ios valueForKey:@"gameanalyticsGameKey"]
                            gameSecret:[ios valueForKey:@"gameanalyticsSecretKey"]];
}

- (void) setUserInfo:(NSDictionary *) jsonData {
    NSString *gender = [jsonData objectForKey:@"gender"];
    NSString *facebookId = [jsonData objectForKey:@"facebook_id"];

    NSInteger birthYear = [[jsonData objectForKey:@"birthYear"] intValue];

    if(gender == (id)[NSNull null] || gender.length == 0 ) {
        gender = nil;
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
                                        receipt:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"receipt"]]];
}

- (void) newResourceEvent:(NSDictionary *) jsonData {
    [GameAnalytics addResourceEventWithFlowType:(GAResourceFlowType)[[jsonData objectForKey:@"flow_type"] intValue]
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
    [GameAnalytics addProgressionEventWithProgressionStatus:(GAProgressionStatus)[[jsonData objectForKey:@"status"]intValue]
                                              progression01:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"prog_1"]]
                                              progression02:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"prog_2"]]
                                              progression03:[NSString stringWithFormat:@"%@",[jsonData objectForKey:@"prog_3"]]
                                                      score: [[jsonData objectForKey:@"score"] intValue]];
}

- (void) newErrorEvent: (NSDictionary *) jsonData {
    [GameAnalytics addErrorEventWithSeverity:(GAErrorSeverity)[[jsonData objectForKey:@"severity"] intValue]
                                     message:[NSString stringWithFormat:@"%@", [jsonData objectForKey:@"message"]]];
}

@end
