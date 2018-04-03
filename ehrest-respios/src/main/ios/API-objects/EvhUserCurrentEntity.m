//
// EvhUserCurrentEntity.m
//
#import "EvhUserCurrentEntity.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserCurrentEntity
//

@implementation EvhUserCurrentEntity

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserCurrentEntity* obj = [EvhUserCurrentEntity new];
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
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.entityType)
        [jsonObject setObject: self.entityType forKey: @"entityType"];
    if(self.entityId)
        [jsonObject setObject: self.entityId forKey: @"entityId"];
    if(self.groupEntityName)
        [jsonObject setObject: self.groupEntityName forKey: @"groupEntityName"];
    if(self.timestamp)
        [jsonObject setObject: self.timestamp forKey: @"timestamp"];
    if(self.directlyEnterpriseId)
        [jsonObject setObject: self.directlyEnterpriseId forKey: @"directlyEnterpriseId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.entityType = [jsonObject objectForKey: @"entityType"];
        if(self.entityType && [self.entityType isEqual:[NSNull null]])
            self.entityType = nil;

        self.entityId = [jsonObject objectForKey: @"entityId"];
        if(self.entityId && [self.entityId isEqual:[NSNull null]])
            self.entityId = nil;

        self.groupEntityName = [jsonObject objectForKey: @"groupEntityName"];
        if(self.groupEntityName && [self.groupEntityName isEqual:[NSNull null]])
            self.groupEntityName = nil;

        self.timestamp = [jsonObject objectForKey: @"timestamp"];
        if(self.timestamp && [self.timestamp isEqual:[NSNull null]])
            self.timestamp = nil;

        self.directlyEnterpriseId = [jsonObject objectForKey: @"directlyEnterpriseId"];
        if(self.directlyEnterpriseId && [self.directlyEnterpriseId isEqual:[NSNull null]])
            self.directlyEnterpriseId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
