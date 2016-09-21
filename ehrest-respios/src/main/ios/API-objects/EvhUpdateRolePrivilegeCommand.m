//
// EvhUpdateRolePrivilegeCommand.m
//
#import "EvhUpdateRolePrivilegeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateRolePrivilegeCommand
//

@implementation EvhUpdateRolePrivilegeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateRolePrivilegeCommand* obj = [EvhUpdateRolePrivilegeCommand new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.roleId)
        [jsonObject setObject: self.roleId forKey: @"roleId"];
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
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.roleId = [jsonObject objectForKey: @"roleId"];
        if(self.roleId && [self.roleId isEqual:[NSNull null]])
            self.roleId = nil;

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
