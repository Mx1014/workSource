//
// EvhScheduleTaskDTO.m
//
#import "EvhScheduleTaskDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhScheduleTaskDTO
//

@implementation EvhScheduleTaskDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhScheduleTaskDTO* obj = [EvhScheduleTaskDTO new];
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
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.progressData)
        [jsonObject setObject: self.progressData forKey: @"progressData"];
    if(self.resourceType)
        [jsonObject setObject: self.resourceType forKey: @"resourceType"];
    if(self.resourceId)
        [jsonObject setObject: self.resourceId forKey: @"resourceId"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.progress)
        [jsonObject setObject: self.progress forKey: @"progress"];
    if(self.processCount)
        [jsonObject setObject: self.processCount forKey: @"processCount"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.progressData = [jsonObject objectForKey: @"progressData"];
        if(self.progressData && [self.progressData isEqual:[NSNull null]])
            self.progressData = nil;

        self.resourceType = [jsonObject objectForKey: @"resourceType"];
        if(self.resourceType && [self.resourceType isEqual:[NSNull null]])
            self.resourceType = nil;

        self.resourceId = [jsonObject objectForKey: @"resourceId"];
        if(self.resourceId && [self.resourceId isEqual:[NSNull null]])
            self.resourceId = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.progress = [jsonObject objectForKey: @"progress"];
        if(self.progress && [self.progress isEqual:[NSNull null]])
            self.progress = nil;

        self.processCount = [jsonObject objectForKey: @"processCount"];
        if(self.processCount && [self.processCount isEqual:[NSNull null]])
            self.processCount = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
