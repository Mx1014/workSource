//
// EvhDeviceDTO.m
//
#import "EvhDeviceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeviceDTO
//

@implementation EvhDeviceDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeviceDTO* obj = [EvhDeviceDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.deviceId)
        [jsonObject setObject: self.deviceId forKey: @"deviceId"];
    if(self.platform)
        [jsonObject setObject: self.platform forKey: @"platform"];
    if(self.product)
        [jsonObject setObject: self.product forKey: @"product"];
    if(self.brand)
        [jsonObject setObject: self.brand forKey: @"brand"];
    if(self.deviceModel)
        [jsonObject setObject: self.deviceModel forKey: @"deviceModel"];
    if(self.systemVersion)
        [jsonObject setObject: self.systemVersion forKey: @"systemVersion"];
    if(self.meta)
        [jsonObject setObject: self.meta forKey: @"meta"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.deviceId = [jsonObject objectForKey: @"deviceId"];
        if(self.deviceId && [self.deviceId isEqual:[NSNull null]])
            self.deviceId = nil;

        self.platform = [jsonObject objectForKey: @"platform"];
        if(self.platform && [self.platform isEqual:[NSNull null]])
            self.platform = nil;

        self.product = [jsonObject objectForKey: @"product"];
        if(self.product && [self.product isEqual:[NSNull null]])
            self.product = nil;

        self.brand = [jsonObject objectForKey: @"brand"];
        if(self.brand && [self.brand isEqual:[NSNull null]])
            self.brand = nil;

        self.deviceModel = [jsonObject objectForKey: @"deviceModel"];
        if(self.deviceModel && [self.deviceModel isEqual:[NSNull null]])
            self.deviceModel = nil;

        self.systemVersion = [jsonObject objectForKey: @"systemVersion"];
        if(self.systemVersion && [self.systemVersion isEqual:[NSNull null]])
            self.systemVersion = nil;

        self.meta = [jsonObject objectForKey: @"meta"];
        if(self.meta && [self.meta isEqual:[NSNull null]])
            self.meta = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
