#import "GameAnalyticsPlugin.h"

@implementation GameAnalyticsPlugin

// The plugin must call super dealloc.
- (void) dealloc {
  [super dealloc];
}

// The plugin must call super init.
- (id) init {
  self = [super init];
  if (!self) {
    return nil;
  }
  return self;
}


- (void) initializeWithManifest:(NSDictionary *)manifest appDelegate:(TeaLeafAppDelegate *)appDelegate {
  NSDictionary *ios = [manifest valueForKey:@"ios"];

  //To Track crashes
  [GameAnalytics enableExceptionHandler:TRUE];
  [GameAnalytics initializeWithGameKey:[ios valueForKey:@"gameanalyticsGameKey"]
                             secretKey:[ios valueForKey:@"gameanalyticsSecretKey"]];

  //NOTE: REMOVE THIS IN PRODUCTION
  //[GameAnalytics setDebugLogLevelVerbose:TRUE];
}

- (void) setUserInfo:(NSDictionary *) jsonData {
  NSString *gender = [jsonData objectForKey:@"gender"];
  NSNumber *birthYear = [jsonData objectForKey:@"birthYear"];
  NSNumber *friendCount = [jsonData objectForKey:@"friendCount"];

  if(gender == (id)[NSNull null] || gender.length == 0 ) {
    gender = nil;
  }
  if ([birthYear isKindOfClass:[NSNull class]]) {
    birthYear = nil;
  }
  if ([friendCount isKindOfClass:[NSNull class]]) {
    friendCount = nil;
  }

  [GameAnalytics setUserInfoWithGender:gender
                             birthYear:birthYear
                           friendCount:friendCount];
}

- (void) newBusinessEvent:(NSDictionary *) jsonData {
  [GameAnalytics newBusinessEventWithId:[jsonData objectForKey:@"item"]
                               currency:[jsonData objectForKey:@"currency"]
                                 amount:[jsonData objectForKey:@"amount"]];
}

- (void) newDesignEvent:(NSDictionary *) jsonData {
  [GameAnalytics newDesignEventWithId:[jsonData objectForKey:@"eventId"]
                                value:[jsonData objectForKey:@"value"]];
}

- (void) newErrorEvent:(NSDictionary *) jsonData {
  //TODO
}

- (void) setNetworkPollEvent:(NSDictionary *) jsonData {
  //TODO
}

- (void) setSendEventsInterval:(NSDictionary *) jsonData {
  //TODO
  //[GameAnalytics setSendEventsInterval:10];
}

- (void) setSessionTimeout:(NSDictionary *) jsonData {
  //TODO
}

- (void) startFPS:(NSDictionary *) jsonData {
  //TODO
}
- (void)fpsTimerTick:(NSTimer *)timer {
  //[GameAnalytics logFPS];
}
- (void)stopFPS:(NSDictionary *) jsonData {
  //TODO
  //[GameAnalytics stopLoggingFPS];
}

- (void) applicationWillTerminate:(UIApplication *)app {
}

- (void) applicationDidBecomeActive:(UIApplication *)app {
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  return true;
}

- (void) handleOpenURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication {
}

- (void) didBecomeActive:(NSDictionary *)jsonObject {
}
@end
