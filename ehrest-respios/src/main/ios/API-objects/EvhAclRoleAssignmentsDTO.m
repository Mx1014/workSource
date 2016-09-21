//
// EvhAclRoleAssignmentsDTO.m
//
#import "EvhAclRoleAssignmentsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclRoleAssignmentsDTO
//

@implementation EvhAclRoleAssignmentsDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclRoleAssignmentsDTO* obj = [EvhAclRoleAssignmentsDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.targetName)
        [jsonObject setObject: self.targetName forKey: @"targetName"];
    if(self.roleId)
        [jsonObject setObject: self.roleId forKey: @"roleId"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.roleName)
        [jsonObject setObject: self.roleName forKey: @"roleName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

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

        self.targetName = [jsonObject objectForKey: @"targetName"];
        if(self.targetName && [self.targetName isEqual:[NSNull null]])
            self.targetName = nil;

        self.roleId = [jsonObject objectForKey: @"roleId"];
        if(self.roleId && [self.roleId isEqual:[NSNull null]])
            self.roleId = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.roleName = [jsonObject objectForKey: @"roleName"];
        if(self.roleName && [self.roleName isEqual:[NSNull null]])
            self.roleName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
