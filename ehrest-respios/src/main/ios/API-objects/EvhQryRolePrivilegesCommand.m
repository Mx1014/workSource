//
// EvhQryRolePrivilegesCommand.m
//
#import "EvhQryRolePrivilegesCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQryRolePrivilegesCommand
//

@implementation EvhQryRolePrivilegesCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQryRolePrivilegesCommand* obj = [EvhQryRolePrivilegesCommand new];
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
    if(self.roleId)
        [jsonObject setObject: self.roleId forKey: @"roleId"];
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.roleId = [jsonObject objectForKey: @"roleId"];
        if(self.roleId && [self.roleId isEqual:[NSNull null]])
            self.roleId = nil;

        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
