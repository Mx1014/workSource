//
// EvhGroupMemberSnapshotDTO.m
//
#import "EvhGroupMemberSnapshotDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupMemberSnapshotDTO
//

@implementation EvhGroupMemberSnapshotDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGroupMemberSnapshotDTO* obj = [EvhGroupMemberSnapshotDTO new];
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
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.groupName)
        [jsonObject setObject: self.groupName forKey: @"groupName"];
    if(self.memberType)
        [jsonObject setObject: self.memberType forKey: @"memberType"];
    if(self.memberId)
        [jsonObject setObject: self.memberId forKey: @"memberId"];
    if(self.memberNickName)
        [jsonObject setObject: self.memberNickName forKey: @"memberNickName"];
    if(self.memberAvatar)
        [jsonObject setObject: self.memberAvatar forKey: @"memberAvatar"];
    if(self.memberAvatarUrl)
        [jsonObject setObject: self.memberAvatarUrl forKey: @"memberAvatarUrl"];
    if(self.updateTime)
        [jsonObject setObject: self.updateTime forKey: @"updateTime"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.groupName = [jsonObject objectForKey: @"groupName"];
        if(self.groupName && [self.groupName isEqual:[NSNull null]])
            self.groupName = nil;

        self.memberType = [jsonObject objectForKey: @"memberType"];
        if(self.memberType && [self.memberType isEqual:[NSNull null]])
            self.memberType = nil;

        self.memberId = [jsonObject objectForKey: @"memberId"];
        if(self.memberId && [self.memberId isEqual:[NSNull null]])
            self.memberId = nil;

        self.memberNickName = [jsonObject objectForKey: @"memberNickName"];
        if(self.memberNickName && [self.memberNickName isEqual:[NSNull null]])
            self.memberNickName = nil;

        self.memberAvatar = [jsonObject objectForKey: @"memberAvatar"];
        if(self.memberAvatar && [self.memberAvatar isEqual:[NSNull null]])
            self.memberAvatar = nil;

        self.memberAvatarUrl = [jsonObject objectForKey: @"memberAvatarUrl"];
        if(self.memberAvatarUrl && [self.memberAvatarUrl isEqual:[NSNull null]])
            self.memberAvatarUrl = nil;

        self.updateTime = [jsonObject objectForKey: @"updateTime"];
        if(self.updateTime && [self.updateTime isEqual:[NSNull null]])
            self.updateTime = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
