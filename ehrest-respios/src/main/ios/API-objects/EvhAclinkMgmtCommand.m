//
// EvhAclinkMgmtCommand.m
//
#import "EvhAclinkMgmtCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkMgmtCommand
//

@implementation EvhAclinkMgmtCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkMgmtCommand* obj = [EvhAclinkMgmtCommand new];
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
    if(self.wifiSsid)
        [jsonObject setObject: self.wifiSsid forKey: @"wifiSsid"];
    if(self.wifiPwd)
        [jsonObject setObject: self.wifiPwd forKey: @"wifiPwd"];
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.wifiSsid = [jsonObject objectForKey: @"wifiSsid"];
        if(self.wifiSsid && [self.wifiSsid isEqual:[NSNull null]])
            self.wifiSsid = nil;

        self.wifiPwd = [jsonObject objectForKey: @"wifiPwd"];
        if(self.wifiPwd && [self.wifiPwd isEqual:[NSNull null]])
            self.wifiPwd = nil;

        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
