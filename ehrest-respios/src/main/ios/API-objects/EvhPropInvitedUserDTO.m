//
// EvhPropInvitedUserDTO.m
//
#import "EvhPropInvitedUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropInvitedUserDTO
//

@implementation EvhPropInvitedUserDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPropInvitedUserDTO* obj = [EvhPropInvitedUserDTO new];
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
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.inviteType)
        [jsonObject setObject: self.inviteType forKey: @"inviteType"];
    if(self.contactType)
        [jsonObject setObject: self.contactType forKey: @"contactType"];
    if(self.contactToken)
        [jsonObject setObject: self.contactToken forKey: @"contactToken"];
    if(self.registerTime)
        [jsonObject setObject: self.registerTime forKey: @"registerTime"];
    if(self.invitorId)
        [jsonObject setObject: self.invitorId forKey: @"invitorId"];
    if(self.invitorName)
        [jsonObject setObject: self.invitorName forKey: @"invitorName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.inviteType = [jsonObject objectForKey: @"inviteType"];
        if(self.inviteType && [self.inviteType isEqual:[NSNull null]])
            self.inviteType = nil;

        self.contactType = [jsonObject objectForKey: @"contactType"];
        if(self.contactType && [self.contactType isEqual:[NSNull null]])
            self.contactType = nil;

        self.contactToken = [jsonObject objectForKey: @"contactToken"];
        if(self.contactToken && [self.contactToken isEqual:[NSNull null]])
            self.contactToken = nil;

        self.registerTime = [jsonObject objectForKey: @"registerTime"];
        if(self.registerTime && [self.registerTime isEqual:[NSNull null]])
            self.registerTime = nil;

        self.invitorId = [jsonObject objectForKey: @"invitorId"];
        if(self.invitorId && [self.invitorId isEqual:[NSNull null]])
            self.invitorId = nil;

        self.invitorName = [jsonObject objectForKey: @"invitorName"];
        if(self.invitorName && [self.invitorName isEqual:[NSNull null]])
            self.invitorName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
