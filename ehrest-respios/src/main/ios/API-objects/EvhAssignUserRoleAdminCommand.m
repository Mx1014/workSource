//
// EvhAssignUserRoleAdminCommand.m
//
#import "EvhAssignUserRoleAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAssignUserRoleAdminCommand
//

@implementation EvhAssignUserRoleAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAssignUserRoleAdminCommand* obj = [EvhAssignUserRoleAdminCommand new];
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
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.roleId)
        [jsonObject setObject: self.roleId forKey: @"roleId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.roleId = [jsonObject objectForKey: @"roleId"];
        if(self.roleId && [self.roleId isEqual:[NSNull null]])
            self.roleId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
