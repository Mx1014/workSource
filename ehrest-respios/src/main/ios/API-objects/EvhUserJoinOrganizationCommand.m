//
// EvhUserJoinOrganizationCommand.m
//
#import "EvhUserJoinOrganizationCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserJoinOrganizationCommand
//

@implementation EvhUserJoinOrganizationCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserJoinOrganizationCommand* obj = [EvhUserJoinOrganizationCommand new];
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
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.areaId)
        [jsonObject setObject: self.areaId forKey: @"areaId"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.orgType)
        [jsonObject setObject: self.orgType forKey: @"orgType"];
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.userJoin)
        [jsonObject setObject: self.userJoin forKey: @"userJoin"];
    if(self.memberType)
        [jsonObject setObject: self.memberType forKey: @"memberType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.areaId = [jsonObject objectForKey: @"areaId"];
        if(self.areaId && [self.areaId isEqual:[NSNull null]])
            self.areaId = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.orgType = [jsonObject objectForKey: @"orgType"];
        if(self.orgType && [self.orgType isEqual:[NSNull null]])
            self.orgType = nil;

        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.userJoin = [jsonObject objectForKey: @"userJoin"];
        if(self.userJoin && [self.userJoin isEqual:[NSNull null]])
            self.userJoin = nil;

        self.memberType = [jsonObject objectForKey: @"memberType"];
        if(self.memberType && [self.memberType isEqual:[NSNull null]])
            self.memberType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
