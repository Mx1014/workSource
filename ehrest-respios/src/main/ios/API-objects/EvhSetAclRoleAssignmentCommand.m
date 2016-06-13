//
// EvhSetAclRoleAssignmentCommand.m
//
#import "EvhSetAclRoleAssignmentCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetAclRoleAssignmentCommand
//

@implementation EvhSetAclRoleAssignmentCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetAclRoleAssignmentCommand* obj = [EvhSetAclRoleAssignmentCommand new];
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
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.roleId)
        [jsonObject setObject: self.roleId forKey: @"roleId"];
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

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
