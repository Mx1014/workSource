//
// EvhSyncActivityCommand.m
//
#import "EvhSyncActivityCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncActivityCommand
//

@implementation EvhSyncActivityCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSyncActivityCommand* obj = [EvhSyncActivityCommand new];
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
    if(self.activityType)
        [jsonObject setObject: self.activityType forKey: @"activityType"];
    if(self.appVersionCode)
        [jsonObject setObject: self.appVersionCode forKey: @"appVersionCode"];
    if(self.appVersionName)
        [jsonObject setObject: self.appVersionName forKey: @"appVersionName"];
    if(self.channelId)
        [jsonObject setObject: self.channelId forKey: @"channelId"];
    if(self.imeiNumber)
        [jsonObject setObject: self.imeiNumber forKey: @"imeiNumber"];
    if(self.deviceType)
        [jsonObject setObject: self.deviceType forKey: @"deviceType"];
    if(self.osInfo)
        [jsonObject setObject: self.osInfo forKey: @"osInfo"];
    if(self.osType)
        [jsonObject setObject: self.osType forKey: @"osType"];
    if(self.mktDataVersion)
        [jsonObject setObject: self.mktDataVersion forKey: @"mktDataVersion"];
    if(self.reportConfigVersion)
        [jsonObject setObject: self.reportConfigVersion forKey: @"reportConfigVersion"];
    if(self.internalIp)
        [jsonObject setObject: self.internalIp forKey: @"internalIp"];
    if(self.collectTimeMillis)
        [jsonObject setObject: self.collectTimeMillis forKey: @"collectTimeMillis"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.activityType = [jsonObject objectForKey: @"activityType"];
        if(self.activityType && [self.activityType isEqual:[NSNull null]])
            self.activityType = nil;

        self.appVersionCode = [jsonObject objectForKey: @"appVersionCode"];
        if(self.appVersionCode && [self.appVersionCode isEqual:[NSNull null]])
            self.appVersionCode = nil;

        self.appVersionName = [jsonObject objectForKey: @"appVersionName"];
        if(self.appVersionName && [self.appVersionName isEqual:[NSNull null]])
            self.appVersionName = nil;

        self.channelId = [jsonObject objectForKey: @"channelId"];
        if(self.channelId && [self.channelId isEqual:[NSNull null]])
            self.channelId = nil;

        self.imeiNumber = [jsonObject objectForKey: @"imeiNumber"];
        if(self.imeiNumber && [self.imeiNumber isEqual:[NSNull null]])
            self.imeiNumber = nil;

        self.deviceType = [jsonObject objectForKey: @"deviceType"];
        if(self.deviceType && [self.deviceType isEqual:[NSNull null]])
            self.deviceType = nil;

        self.osInfo = [jsonObject objectForKey: @"osInfo"];
        if(self.osInfo && [self.osInfo isEqual:[NSNull null]])
            self.osInfo = nil;

        self.osType = [jsonObject objectForKey: @"osType"];
        if(self.osType && [self.osType isEqual:[NSNull null]])
            self.osType = nil;

        self.mktDataVersion = [jsonObject objectForKey: @"mktDataVersion"];
        if(self.mktDataVersion && [self.mktDataVersion isEqual:[NSNull null]])
            self.mktDataVersion = nil;

        self.reportConfigVersion = [jsonObject objectForKey: @"reportConfigVersion"];
        if(self.reportConfigVersion && [self.reportConfigVersion isEqual:[NSNull null]])
            self.reportConfigVersion = nil;

        self.internalIp = [jsonObject objectForKey: @"internalIp"];
        if(self.internalIp && [self.internalIp isEqual:[NSNull null]])
            self.internalIp = nil;

        self.collectTimeMillis = [jsonObject objectForKey: @"collectTimeMillis"];
        if(self.collectTimeMillis && [self.collectTimeMillis isEqual:[NSNull null]])
            self.collectTimeMillis = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
