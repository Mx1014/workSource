//
// EvhUpdatePushMessageCommand.m
//
#import "EvhUpdatePushMessageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdatePushMessageCommand
//

@implementation EvhUpdatePushMessageCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdatePushMessageCommand* obj = [EvhUpdatePushMessageCommand new];
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
    if(self.messageType)
        [jsonObject setObject: self.messageType forKey: @"messageType"];
    if(self.title)
        [jsonObject setObject: self.title forKey: @"title"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.deviceType)
        [jsonObject setObject: self.deviceType forKey: @"deviceType"];
    if(self.deviceTag)
        [jsonObject setObject: self.deviceTag forKey: @"deviceTag"];
    if(self.appVersion)
        [jsonObject setObject: self.appVersion forKey: @"appVersion"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.messageType = [jsonObject objectForKey: @"messageType"];
        if(self.messageType && [self.messageType isEqual:[NSNull null]])
            self.messageType = nil;

        self.title = [jsonObject objectForKey: @"title"];
        if(self.title && [self.title isEqual:[NSNull null]])
            self.title = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.deviceType = [jsonObject objectForKey: @"deviceType"];
        if(self.deviceType && [self.deviceType isEqual:[NSNull null]])
            self.deviceType = nil;

        self.deviceTag = [jsonObject objectForKey: @"deviceTag"];
        if(self.deviceTag && [self.deviceTag isEqual:[NSNull null]])
            self.deviceTag = nil;

        self.appVersion = [jsonObject objectForKey: @"appVersion"];
        if(self.appVersion && [self.appVersion isEqual:[NSNull null]])
            self.appVersion = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
