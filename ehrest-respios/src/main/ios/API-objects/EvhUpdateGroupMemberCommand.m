//
// EvhUpdateGroupMemberCommand.m
//
#import "EvhUpdateGroupMemberCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateGroupMemberCommand
//

@implementation EvhUpdateGroupMemberCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateGroupMemberCommand* obj = [EvhUpdateGroupMemberCommand new];
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
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.memberId)
        [jsonObject setObject: self.memberId forKey: @"memberId"];
    if(self.memberNickName)
        [jsonObject setObject: self.memberNickName forKey: @"memberNickName"];
    if(self.memberAvatar)
        [jsonObject setObject: self.memberAvatar forKey: @"memberAvatar"];
    if(self.phonePrivateFlag)
        [jsonObject setObject: self.phonePrivateFlag forKey: @"phonePrivateFlag"];
    if(self.muteNotificationFlag)
        [jsonObject setObject: self.muteNotificationFlag forKey: @"muteNotificationFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.memberId = [jsonObject objectForKey: @"memberId"];
        if(self.memberId && [self.memberId isEqual:[NSNull null]])
            self.memberId = nil;

        self.memberNickName = [jsonObject objectForKey: @"memberNickName"];
        if(self.memberNickName && [self.memberNickName isEqual:[NSNull null]])
            self.memberNickName = nil;

        self.memberAvatar = [jsonObject objectForKey: @"memberAvatar"];
        if(self.memberAvatar && [self.memberAvatar isEqual:[NSNull null]])
            self.memberAvatar = nil;

        self.phonePrivateFlag = [jsonObject objectForKey: @"phonePrivateFlag"];
        if(self.phonePrivateFlag && [self.phonePrivateFlag isEqual:[NSNull null]])
            self.phonePrivateFlag = nil;

        self.muteNotificationFlag = [jsonObject objectForKey: @"muteNotificationFlag"];
        if(self.muteNotificationFlag && [self.muteNotificationFlag isEqual:[NSNull null]])
            self.muteNotificationFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
