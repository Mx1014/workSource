//
// EvhPushMessageDTO.m
//
#import "EvhPushMessageDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPushMessageDTO
//

@implementation EvhPushMessageDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPushMessageDTO* obj = [EvhPushMessageDTO new];
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
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.finishTime)
        [jsonObject setObject: self.finishTime forKey: @"finishTime"];
    if(self.deviceType)
        [jsonObject setObject: self.deviceType forKey: @"deviceType"];
    if(self.deviceTag)
        [jsonObject setObject: self.deviceTag forKey: @"deviceTag"];
    if(self.appVersion)
        [jsonObject setObject: self.appVersion forKey: @"appVersion"];
    if(self.pushCount)
        [jsonObject setObject: self.pushCount forKey: @"pushCount"];
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

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.finishTime = [jsonObject objectForKey: @"finishTime"];
        if(self.finishTime && [self.finishTime isEqual:[NSNull null]])
            self.finishTime = nil;

        self.deviceType = [jsonObject objectForKey: @"deviceType"];
        if(self.deviceType && [self.deviceType isEqual:[NSNull null]])
            self.deviceType = nil;

        self.deviceTag = [jsonObject objectForKey: @"deviceTag"];
        if(self.deviceTag && [self.deviceTag isEqual:[NSNull null]])
            self.deviceTag = nil;

        self.appVersion = [jsonObject objectForKey: @"appVersion"];
        if(self.appVersion && [self.appVersion isEqual:[NSNull null]])
            self.appVersion = nil;

        self.pushCount = [jsonObject objectForKey: @"pushCount"];
        if(self.pushCount && [self.pushCount isEqual:[NSNull null]])
            self.pushCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
