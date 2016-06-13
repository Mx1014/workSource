//
// EvhOpPromotionActivityDTO.m
//
#import "EvhOpPromotionActivityDTO.h"
#import "EvhOpPromotionAssignedScopeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionActivityDTO
//

@implementation EvhOpPromotionActivityDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOpPromotionActivityDTO* obj = [EvhOpPromotionActivityDTO new];
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
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.pushCount)
        [jsonObject setObject: self.pushCount forKey: @"pushCount"];
    if(self.policyData)
        [jsonObject setObject: self.policyData forKey: @"policyData"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.title)
        [jsonObject setObject: self.title forKey: @"title"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
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
    if(self.processStatus)
        [jsonObject setObject: self.processStatus forKey: @"processStatus"];
    if(self.policyType)
        [jsonObject setObject: self.policyType forKey: @"policyType"];
    if(self.iconUri)
        [jsonObject setObject: self.iconUri forKey: @"iconUri"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.processCount)
        [jsonObject setObject: self.processCount forKey: @"processCount"];
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
        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.pushCount = [jsonObject objectForKey: @"pushCount"];
        if(self.pushCount && [self.pushCount isEqual:[NSNull null]])
            self.pushCount = nil;

        self.policyData = [jsonObject objectForKey: @"policyData"];
        if(self.policyData && [self.policyData isEqual:[NSNull null]])
            self.policyData = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.title = [jsonObject objectForKey: @"title"];
        if(self.title && [self.title isEqual:[NSNull null]])
            self.title = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

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

        self.processStatus = [jsonObject objectForKey: @"processStatus"];
        if(self.processStatus && [self.processStatus isEqual:[NSNull null]])
            self.processStatus = nil;

        self.policyType = [jsonObject objectForKey: @"policyType"];
        if(self.policyType && [self.policyType isEqual:[NSNull null]])
            self.policyType = nil;

        self.iconUri = [jsonObject objectForKey: @"iconUri"];
        if(self.iconUri && [self.iconUri isEqual:[NSNull null]])
            self.iconUri = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.processCount = [jsonObject objectForKey: @"processCount"];
        if(self.processCount && [self.processCount isEqual:[NSNull null]])
            self.processCount = nil;

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
