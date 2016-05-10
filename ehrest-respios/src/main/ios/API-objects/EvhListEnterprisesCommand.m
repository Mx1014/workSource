//
// EvhListEnterprisesCommand.m
//
#import "EvhListEnterprisesCommand.h"
#import "EvhBoolean.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterprisesCommand
//

@implementation EvhListEnterprisesCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListEnterprisesCommand* obj = [EvhListEnterprisesCommand new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.buildingId)
        [jsonObject setObject: self.buildingId forKey: @"buildingId"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.qryAdminRoleFlag) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.qryAdminRoleFlag toJson: dic];
        
        [jsonObject setObject: dic forKey: @"qryAdminRoleFlag"];
    }
    if(self.keywords)
        [jsonObject setObject: self.keywords forKey: @"keywords"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.buildingId = [jsonObject objectForKey: @"buildingId"];
        if(self.buildingId && [self.buildingId isEqual:[NSNull null]])
            self.buildingId = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"qryAdminRoleFlag"];

        self.qryAdminRoleFlag = [EvhBoolean new];
        self.qryAdminRoleFlag = [self.qryAdminRoleFlag fromJson: itemJson];
        self.keywords = [jsonObject objectForKey: @"keywords"];
        if(self.keywords && [self.keywords isEqual:[NSNull null]])
            self.keywords = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
