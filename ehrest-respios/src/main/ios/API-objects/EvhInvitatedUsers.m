//
// EvhInvitatedUsers.m
//
#import "EvhInvitatedUsers.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInvitatedUsers
//

@implementation EvhInvitatedUsers

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhInvitatedUsers* obj = [EvhInvitatedUsers new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.inviterId)
        [jsonObject setObject: self.inviterId forKey: @"inviterId"];
    if(self.userNickName)
        [jsonObject setObject: self.userNickName forKey: @"userNickName"];
    if(self.userCellPhone)
        [jsonObject setObject: self.userCellPhone forKey: @"userCellPhone"];
    if(self.registerTime)
        [jsonObject setObject: self.registerTime forKey: @"registerTime"];
    if(self.inviteType)
        [jsonObject setObject: self.inviteType forKey: @"inviteType"];
    if(self.inviter)
        [jsonObject setObject: self.inviter forKey: @"inviter"];
    if(self.inviterCellPhone)
        [jsonObject setObject: self.inviterCellPhone forKey: @"inviterCellPhone"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.inviterId = [jsonObject objectForKey: @"inviterId"];
        if(self.inviterId && [self.inviterId isEqual:[NSNull null]])
            self.inviterId = nil;

        self.userNickName = [jsonObject objectForKey: @"userNickName"];
        if(self.userNickName && [self.userNickName isEqual:[NSNull null]])
            self.userNickName = nil;

        self.userCellPhone = [jsonObject objectForKey: @"userCellPhone"];
        if(self.userCellPhone && [self.userCellPhone isEqual:[NSNull null]])
            self.userCellPhone = nil;

        self.registerTime = [jsonObject objectForKey: @"registerTime"];
        if(self.registerTime && [self.registerTime isEqual:[NSNull null]])
            self.registerTime = nil;

        self.inviteType = [jsonObject objectForKey: @"inviteType"];
        if(self.inviteType && [self.inviteType isEqual:[NSNull null]])
            self.inviteType = nil;

        self.inviter = [jsonObject objectForKey: @"inviter"];
        if(self.inviter && [self.inviter isEqual:[NSNull null]])
            self.inviter = nil;

        self.inviterCellPhone = [jsonObject objectForKey: @"inviterCellPhone"];
        if(self.inviterCellPhone && [self.inviterCellPhone isEqual:[NSNull null]])
            self.inviterCellPhone = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
