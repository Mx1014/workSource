//
// EvhOpPromotionDTO.m
//
#import "EvhOpPromotionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionDTO
//

@implementation EvhOpPromotionDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOpPromotionDTO* obj = [EvhOpPromotionDTO new];
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
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.sceneType)
        [jsonObject setObject: self.sceneType forKey: @"sceneType"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.actionType)
        [jsonObject setObject: self.actionType forKey: @"actionType"];
    if(self.actionData)
        [jsonObject setObject: self.actionData forKey: @"actionData"];
    if(self.validCount)
        [jsonObject setObject: self.validCount forKey: @"validCount"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.sceneType = [jsonObject objectForKey: @"sceneType"];
        if(self.sceneType && [self.sceneType isEqual:[NSNull null]])
            self.sceneType = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.actionType = [jsonObject objectForKey: @"actionType"];
        if(self.actionType && [self.actionType isEqual:[NSNull null]])
            self.actionType = nil;

        self.actionData = [jsonObject objectForKey: @"actionData"];
        if(self.actionData && [self.actionData isEqual:[NSNull null]])
            self.actionData = nil;

        self.validCount = [jsonObject objectForKey: @"validCount"];
        if(self.validCount && [self.validCount isEqual:[NSNull null]])
            self.validCount = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
