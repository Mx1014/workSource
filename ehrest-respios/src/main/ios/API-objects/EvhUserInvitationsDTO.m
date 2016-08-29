//
// EvhUserInvitationsDTO.m
//
#import "EvhUserInvitationsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserInvitationsDTO
//

@implementation EvhUserInvitationsDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserInvitationsDTO* obj = [EvhUserInvitationsDTO new];
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
    if(self.ownerUid)
        [jsonObject setObject: self.ownerUid forKey: @"ownerUid"];
    if(self.inviteCode)
        [jsonObject setObject: self.inviteCode forKey: @"inviteCode"];
    if(self.inviteType)
        [jsonObject setObject: self.inviteType forKey: @"inviteType"];
    if(self.expiration)
        [jsonObject setObject: self.expiration forKey: @"expiration"];
    if(self.targetEntityType)
        [jsonObject setObject: self.targetEntityType forKey: @"targetEntityType"];
    if(self.targetEntityId)
        [jsonObject setObject: self.targetEntityId forKey: @"targetEntityId"];
    if(self.maxInviteCount)
        [jsonObject setObject: self.maxInviteCount forKey: @"maxInviteCount"];
    if(self.currentInviteCount)
        [jsonObject setObject: self.currentInviteCount forKey: @"currentInviteCount"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerUid = [jsonObject objectForKey: @"ownerUid"];
        if(self.ownerUid && [self.ownerUid isEqual:[NSNull null]])
            self.ownerUid = nil;

        self.inviteCode = [jsonObject objectForKey: @"inviteCode"];
        if(self.inviteCode && [self.inviteCode isEqual:[NSNull null]])
            self.inviteCode = nil;

        self.inviteType = [jsonObject objectForKey: @"inviteType"];
        if(self.inviteType && [self.inviteType isEqual:[NSNull null]])
            self.inviteType = nil;

        self.expiration = [jsonObject objectForKey: @"expiration"];
        if(self.expiration && [self.expiration isEqual:[NSNull null]])
            self.expiration = nil;

        self.targetEntityType = [jsonObject objectForKey: @"targetEntityType"];
        if(self.targetEntityType && [self.targetEntityType isEqual:[NSNull null]])
            self.targetEntityType = nil;

        self.targetEntityId = [jsonObject objectForKey: @"targetEntityId"];
        if(self.targetEntityId && [self.targetEntityId isEqual:[NSNull null]])
            self.targetEntityId = nil;

        self.maxInviteCount = [jsonObject objectForKey: @"maxInviteCount"];
        if(self.maxInviteCount && [self.maxInviteCount isEqual:[NSNull null]])
            self.maxInviteCount = nil;

        self.currentInviteCount = [jsonObject objectForKey: @"currentInviteCount"];
        if(self.currentInviteCount && [self.currentInviteCount isEqual:[NSNull null]])
            self.currentInviteCount = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
