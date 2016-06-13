//
// EvhCreateOpPromotionCommand.m
//
#import "EvhCreateOpPromotionCommand.h"
#import "EvhOpPromotionAssignedScopeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOpPromotionCommand
//

@implementation EvhCreateOpPromotionCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateOpPromotionCommand* obj = [EvhCreateOpPromotionCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _assignedScopes = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.title)
        [jsonObject setObject: self.title forKey: @"title"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.actionData)
        [jsonObject setObject: self.actionData forKey: @"actionData"];
    if(self.actionType)
        [jsonObject setObject: self.actionType forKey: @"actionType"];
    if(self.pushMessage)
        [jsonObject setObject: self.pushMessage forKey: @"pushMessage"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.policyType)
        [jsonObject setObject: self.policyType forKey: @"policyType"];
    if(self.policyData)
        [jsonObject setObject: self.policyData forKey: @"policyData"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.assignedScopes) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhOpPromotionAssignedScopeDTO* item in self.assignedScopes) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"assignedScopes"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.title = [jsonObject objectForKey: @"title"];
        if(self.title && [self.title isEqual:[NSNull null]])
            self.title = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.actionData = [jsonObject objectForKey: @"actionData"];
        if(self.actionData && [self.actionData isEqual:[NSNull null]])
            self.actionData = nil;

        self.actionType = [jsonObject objectForKey: @"actionType"];
        if(self.actionType && [self.actionType isEqual:[NSNull null]])
            self.actionType = nil;

        self.pushMessage = [jsonObject objectForKey: @"pushMessage"];
        if(self.pushMessage && [self.pushMessage isEqual:[NSNull null]])
            self.pushMessage = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.policyType = [jsonObject objectForKey: @"policyType"];
        if(self.policyType && [self.policyType isEqual:[NSNull null]])
            self.policyType = nil;

        self.policyData = [jsonObject objectForKey: @"policyData"];
        if(self.policyData && [self.policyData isEqual:[NSNull null]])
            self.policyData = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"assignedScopes"];
            for(id itemJson in jsonArray) {
                EvhOpPromotionAssignedScopeDTO* item = [EvhOpPromotionAssignedScopeDTO new];
                
                [item fromJson: itemJson];
                [self.assignedScopes addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
