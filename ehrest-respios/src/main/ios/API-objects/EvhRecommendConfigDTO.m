//
// EvhRecommendConfigDTO.m
//
#import "EvhRecommendConfigDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendConfigDTO
//

@implementation EvhRecommendConfigDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRecommendConfigDTO* obj = [EvhRecommendConfigDTO new];
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
    if(self.appid)
        [jsonObject setObject: self.appid forKey: @"appid"];
    if(self.suggestType)
        [jsonObject setObject: self.suggestType forKey: @"suggestType"];
    if(self.sourceType)
        [jsonObject setObject: self.sourceType forKey: @"sourceType"];
    if(self.sourceId)
        [jsonObject setObject: self.sourceId forKey: @"sourceId"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.periodType)
        [jsonObject setObject: self.periodType forKey: @"periodType"];
    if(self.periodValue)
        [jsonObject setObject: self.periodValue forKey: @"periodValue"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.runningTime)
        [jsonObject setObject: self.runningTime forKey: @"runningTime"];
    if(self.expireTime)
        [jsonObject setObject: self.expireTime forKey: @"expireTime"];
    if(self.embeddedJson)
        [jsonObject setObject: self.embeddedJson forKey: @"embeddedJson"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.appid = [jsonObject objectForKey: @"appid"];
        if(self.appid && [self.appid isEqual:[NSNull null]])
            self.appid = nil;

        self.suggestType = [jsonObject objectForKey: @"suggestType"];
        if(self.suggestType && [self.suggestType isEqual:[NSNull null]])
            self.suggestType = nil;

        self.sourceType = [jsonObject objectForKey: @"sourceType"];
        if(self.sourceType && [self.sourceType isEqual:[NSNull null]])
            self.sourceType = nil;

        self.sourceId = [jsonObject objectForKey: @"sourceId"];
        if(self.sourceId && [self.sourceId isEqual:[NSNull null]])
            self.sourceId = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.periodType = [jsonObject objectForKey: @"periodType"];
        if(self.periodType && [self.periodType isEqual:[NSNull null]])
            self.periodType = nil;

        self.periodValue = [jsonObject objectForKey: @"periodValue"];
        if(self.periodValue && [self.periodValue isEqual:[NSNull null]])
            self.periodValue = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.runningTime = [jsonObject objectForKey: @"runningTime"];
        if(self.runningTime && [self.runningTime isEqual:[NSNull null]])
            self.runningTime = nil;

        self.expireTime = [jsonObject objectForKey: @"expireTime"];
        if(self.expireTime && [self.expireTime isEqual:[NSNull null]])
            self.expireTime = nil;

        self.embeddedJson = [jsonObject objectForKey: @"embeddedJson"];
        if(self.embeddedJson && [self.embeddedJson isEqual:[NSNull null]])
            self.embeddedJson = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
