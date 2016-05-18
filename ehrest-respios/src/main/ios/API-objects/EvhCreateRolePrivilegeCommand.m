//
// EvhCreateRolePrivilegeCommand.m
//
#import "EvhCreateRolePrivilegeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateRolePrivilegeCommand
//

@implementation EvhCreateRolePrivilegeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateRolePrivilegeCommand* obj = [EvhCreateRolePrivilegeCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _privilegeIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.roleName)
        [jsonObject setObject: self.roleName forKey: @"roleName"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.privilegeIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.privilegeIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"privilegeIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        self.roleName = [jsonObject objectForKey: @"roleName"];
        if(self.roleName && [self.roleName isEqual:[NSNull null]])
            self.roleName = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"privilegeIds"];
            for(id itemJson in jsonArray) {
                [self.privilegeIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
