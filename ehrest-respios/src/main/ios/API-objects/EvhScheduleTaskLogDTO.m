//
// EvhScheduleTaskLogDTO.m
//
#import "EvhScheduleTaskLogDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhScheduleTaskLogDTO
//

@implementation EvhScheduleTaskLogDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhScheduleTaskLogDTO* obj = [EvhScheduleTaskLogDTO new];
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
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.resourceType)
        [jsonObject setObject: self.resourceType forKey: @"resourceType"];
    if(self.resourceId)
        [jsonObject setObject: self.resourceId forKey: @"resourceId"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.taskId)
        [jsonObject setObject: self.taskId forKey: @"taskId"];
    if(self.resultData)
        [jsonObject setObject: self.resultData forKey: @"resultData"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.resourceType = [jsonObject objectForKey: @"resourceType"];
        if(self.resourceType && [self.resourceType isEqual:[NSNull null]])
            self.resourceType = nil;

        self.resourceId = [jsonObject objectForKey: @"resourceId"];
        if(self.resourceId && [self.resourceId isEqual:[NSNull null]])
            self.resourceId = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.taskId = [jsonObject objectForKey: @"taskId"];
        if(self.taskId && [self.taskId isEqual:[NSNull null]])
            self.taskId = nil;

        self.resultData = [jsonObject objectForKey: @"resultData"];
        if(self.resultData && [self.resultData isEqual:[NSNull null]])
            self.resultData = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
