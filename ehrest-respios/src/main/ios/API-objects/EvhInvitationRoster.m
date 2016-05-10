//
// EvhInvitationRoster.m
//
#import "EvhInvitationRoster.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInvitationRoster
//

@implementation EvhInvitationRoster

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhInvitationRoster* obj = [EvhInvitationRoster new];
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
    if(self.uid)
        [jsonObject setObject: self.uid forKey: @"uid"];
    if(self.inviteType)
        [jsonObject setObject: self.inviteType forKey: @"inviteType"];
    if(self.userNickName)
        [jsonObject setObject: self.userNickName forKey: @"userNickName"];
    if(self.regTime)
        [jsonObject setObject: self.regTime forKey: @"regTime"];
    if(self.inviterId)
        [jsonObject setObject: self.inviterId forKey: @"inviterId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.uid = [jsonObject objectForKey: @"uid"];
        if(self.uid && [self.uid isEqual:[NSNull null]])
            self.uid = nil;

        self.inviteType = [jsonObject objectForKey: @"inviteType"];
        if(self.inviteType && [self.inviteType isEqual:[NSNull null]])
            self.inviteType = nil;

        self.userNickName = [jsonObject objectForKey: @"userNickName"];
        if(self.userNickName && [self.userNickName isEqual:[NSNull null]])
            self.userNickName = nil;

        self.regTime = [jsonObject objectForKey: @"regTime"];
        if(self.regTime && [self.regTime isEqual:[NSNull null]])
            self.regTime = nil;

        self.inviterId = [jsonObject objectForKey: @"inviterId"];
        if(self.inviterId && [self.inviterId isEqual:[NSNull null]])
            self.inviterId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
