//
// EvhAppInfo.m
//
#import "EvhAppInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppInfo
//

@implementation EvhAppInfo

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAppInfo* obj = [EvhAppInfo new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.appName)
        [jsonObject setObject: self.appName forKey: @"appName"];
    if(self.appVersion)
        [jsonObject setObject: self.appVersion forKey: @"appVersion"];
    if(self.appSize)
        [jsonObject setObject: self.appSize forKey: @"appSize"];
    if(self.appInstalledTime)
        [jsonObject setObject: self.appInstalledTime forKey: @"appInstalledTime"];
    if(self.collectTimeMs)
        [jsonObject setObject: self.collectTimeMs forKey: @"collectTimeMs"];
    if(self.reportTimeMs)
        [jsonObject setObject: self.reportTimeMs forKey: @"reportTimeMs"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.appName = [jsonObject objectForKey: @"appName"];
        if(self.appName && [self.appName isEqual:[NSNull null]])
            self.appName = nil;

        self.appVersion = [jsonObject objectForKey: @"appVersion"];
        if(self.appVersion && [self.appVersion isEqual:[NSNull null]])
            self.appVersion = nil;

        self.appSize = [jsonObject objectForKey: @"appSize"];
        if(self.appSize && [self.appSize isEqual:[NSNull null]])
            self.appSize = nil;

        self.appInstalledTime = [jsonObject objectForKey: @"appInstalledTime"];
        if(self.appInstalledTime && [self.appInstalledTime isEqual:[NSNull null]])
            self.appInstalledTime = nil;

        self.collectTimeMs = [jsonObject objectForKey: @"collectTimeMs"];
        if(self.collectTimeMs && [self.collectTimeMs isEqual:[NSNull null]])
            self.collectTimeMs = nil;

        self.reportTimeMs = [jsonObject objectForKey: @"reportTimeMs"];
        if(self.reportTimeMs && [self.reportTimeMs isEqual:[NSNull null]])
            self.reportTimeMs = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
