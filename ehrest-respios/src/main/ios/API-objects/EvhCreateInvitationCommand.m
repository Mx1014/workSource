//
// EvhCreateInvitationCommand.m
//
#import "EvhCreateInvitationCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateInvitationCommand
//

@implementation EvhCreateInvitationCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateInvitationCommand* obj = [EvhCreateInvitationCommand new];
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
    if(self.inviteType)
        [jsonObject setObject: self.inviteType forKey: @"inviteType"];
    if(self.targetEntityType)
        [jsonObject setObject: self.targetEntityType forKey: @"targetEntityType"];
    if(self.targetEntityId)
        [jsonObject setObject: self.targetEntityId forKey: @"targetEntityId"];
    if(self.identifiers)
        [jsonObject setObject: self.identifiers forKey: @"identifiers"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.buildingNum)
        [jsonObject setObject: self.buildingNum forKey: @"buildingNum"];
    if(self.aptNumber)
        [jsonObject setObject: self.aptNumber forKey: @"aptNumber"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.inviteType = [jsonObject objectForKey: @"inviteType"];
        if(self.inviteType && [self.inviteType isEqual:[NSNull null]])
            self.inviteType = nil;

        self.targetEntityType = [jsonObject objectForKey: @"targetEntityType"];
        if(self.targetEntityType && [self.targetEntityType isEqual:[NSNull null]])
            self.targetEntityType = nil;

        self.targetEntityId = [jsonObject objectForKey: @"targetEntityId"];
        if(self.targetEntityId && [self.targetEntityId isEqual:[NSNull null]])
            self.targetEntityId = nil;

        self.identifiers = [jsonObject objectForKey: @"identifiers"];
        if(self.identifiers && [self.identifiers isEqual:[NSNull null]])
            self.identifiers = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.buildingNum = [jsonObject objectForKey: @"buildingNum"];
        if(self.buildingNum && [self.buildingNum isEqual:[NSNull null]])
            self.buildingNum = nil;

        self.aptNumber = [jsonObject objectForKey: @"aptNumber"];
        if(self.aptNumber && [self.aptNumber isEqual:[NSNull null]])
            self.aptNumber = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
