//
// EvhDeviceMessage.m
//
#import "EvhDeviceMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeviceMessage
//

@implementation EvhDeviceMessage

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeviceMessage* obj = [EvhDeviceMessage new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _extra = [NSMutableDictionary new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.alert)
        [jsonObject setObject: self.alert forKey: @"alert"];
    if(self.title)
        [jsonObject setObject: self.title forKey: @"title"];
    if(self.icon)
        [jsonObject setObject: self.icon forKey: @"icon"];
    if(self.audio)
        [jsonObject setObject: self.audio forKey: @"audio"];
    if(self.alertType)
        [jsonObject setObject: self.alertType forKey: @"alertType"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.timeLive)
        [jsonObject setObject: self.timeLive forKey: @"timeLive"];
    if(self.action)
        [jsonObject setObject: self.action forKey: @"action"];
    if(self.appId)
        [jsonObject setObject: self.appId forKey: @"appId"];
    if(self.badge)
        [jsonObject setObject: self.badge forKey: @"badge"];
    if(self.extra) {
        NSMutableDictionary* jsonMap = [NSMutableDictionary new];
        for(NSString* key in self.extra) {
            [jsonMap setValue:[self.extra objectForKey: key] forKey: key];
        }
        [jsonObject setObject: jsonMap forKey: @"extra"];
    }        
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.alert = [jsonObject objectForKey: @"alert"];
        if(self.alert && [self.alert isEqual:[NSNull null]])
            self.alert = nil;

        self.title = [jsonObject objectForKey: @"title"];
        if(self.title && [self.title isEqual:[NSNull null]])
            self.title = nil;

        self.icon = [jsonObject objectForKey: @"icon"];
        if(self.icon && [self.icon isEqual:[NSNull null]])
            self.icon = nil;

        self.audio = [jsonObject objectForKey: @"audio"];
        if(self.audio && [self.audio isEqual:[NSNull null]])
            self.audio = nil;

        self.alertType = [jsonObject objectForKey: @"alertType"];
        if(self.alertType && [self.alertType isEqual:[NSNull null]])
            self.alertType = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.timeLive = [jsonObject objectForKey: @"timeLive"];
        if(self.timeLive && [self.timeLive isEqual:[NSNull null]])
            self.timeLive = nil;

        self.action = [jsonObject objectForKey: @"action"];
        if(self.action && [self.action isEqual:[NSNull null]])
            self.action = nil;

        self.appId = [jsonObject objectForKey: @"appId"];
        if(self.appId && [self.appId isEqual:[NSNull null]])
            self.appId = nil;

        self.badge = [jsonObject objectForKey: @"badge"];
        if(self.badge && [self.badge isEqual:[NSNull null]])
            self.badge = nil;

        {
            NSDictionary* jsonMap = [jsonObject objectForKey: @"extra"];
            for(NSString* key in jsonMap) {
                [self.extra setObject: [jsonMap objectForKey: key] forKey: key];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
