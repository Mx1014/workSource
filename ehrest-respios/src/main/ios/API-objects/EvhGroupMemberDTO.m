//
// EvhGroupMemberDTO.m
//
#import "EvhGroupMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupMemberDTO
//

@implementation EvhGroupMemberDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGroupMemberDTO* obj = [EvhGroupMemberDTO new];
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
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.groupName)
        [jsonObject setObject: self.groupName forKey: @"groupName"];
    if(self.memberType)
        [jsonObject setObject: self.memberType forKey: @"memberType"];
    if(self.memberId)
        [jsonObject setObject: self.memberId forKey: @"memberId"];
    if(self.memberRole)
        [jsonObject setObject: self.memberRole forKey: @"memberRole"];
    if(self.memberNickName)
        [jsonObject setObject: self.memberNickName forKey: @"memberNickName"];
    if(self.memberAvatar)
        [jsonObject setObject: self.memberAvatar forKey: @"memberAvatar"];
    if(self.memberAvatarUrl)
        [jsonObject setObject: self.memberAvatarUrl forKey: @"memberAvatarUrl"];
    if(self.memberStatus)
        [jsonObject setObject: self.memberStatus forKey: @"memberStatus"];
    if(self.inviterUid)
        [jsonObject setObject: self.inviterUid forKey: @"inviterUid"];
    if(self.inviterNickName)
        [jsonObject setObject: self.inviterNickName forKey: @"inviterNickName"];
    if(self.inviterAvatar)
        [jsonObject setObject: self.inviterAvatar forKey: @"inviterAvatar"];
    if(self.inviterAvatarUrl)
        [jsonObject setObject: self.inviterAvatarUrl forKey: @"inviterAvatarUrl"];
    if(self.inviteTime)
        [jsonObject setObject: self.inviteTime forKey: @"inviteTime"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.approveTime)
        [jsonObject setObject: self.approveTime forKey: @"approveTime"];
    if(self.phonePrivateFlag)
        [jsonObject setObject: self.phonePrivateFlag forKey: @"phonePrivateFlag"];
    if(self.cellPhone)
        [jsonObject setObject: self.cellPhone forKey: @"cellPhone"];
    if(self.muteNotificationFlag)
        [jsonObject setObject: self.muteNotificationFlag forKey: @"muteNotificationFlag"];
    if(self.addressId)
        [jsonObject setObject: self.addressId forKey: @"addressId"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.apartmentName)
        [jsonObject setObject: self.apartmentName forKey: @"apartmentName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

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

        self.memberRole = [jsonObject objectForKey: @"memberRole"];
        if(self.memberRole && [self.memberRole isEqual:[NSNull null]])
            self.memberRole = nil;

        self.memberNickName = [jsonObject objectForKey: @"memberNickName"];
        if(self.memberNickName && [self.memberNickName isEqual:[NSNull null]])
            self.memberNickName = nil;

        self.memberAvatar = [jsonObject objectForKey: @"memberAvatar"];
        if(self.memberAvatar && [self.memberAvatar isEqual:[NSNull null]])
            self.memberAvatar = nil;

        self.memberAvatarUrl = [jsonObject objectForKey: @"memberAvatarUrl"];
        if(self.memberAvatarUrl && [self.memberAvatarUrl isEqual:[NSNull null]])
            self.memberAvatarUrl = nil;

        self.memberStatus = [jsonObject objectForKey: @"memberStatus"];
        if(self.memberStatus && [self.memberStatus isEqual:[NSNull null]])
            self.memberStatus = nil;

        self.inviterUid = [jsonObject objectForKey: @"inviterUid"];
        if(self.inviterUid && [self.inviterUid isEqual:[NSNull null]])
            self.inviterUid = nil;

        self.inviterNickName = [jsonObject objectForKey: @"inviterNickName"];
        if(self.inviterNickName && [self.inviterNickName isEqual:[NSNull null]])
            self.inviterNickName = nil;

        self.inviterAvatar = [jsonObject objectForKey: @"inviterAvatar"];
        if(self.inviterAvatar && [self.inviterAvatar isEqual:[NSNull null]])
            self.inviterAvatar = nil;

        self.inviterAvatarUrl = [jsonObject objectForKey: @"inviterAvatarUrl"];
        if(self.inviterAvatarUrl && [self.inviterAvatarUrl isEqual:[NSNull null]])
            self.inviterAvatarUrl = nil;

        self.inviteTime = [jsonObject objectForKey: @"inviteTime"];
        if(self.inviteTime && [self.inviteTime isEqual:[NSNull null]])
            self.inviteTime = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.approveTime = [jsonObject objectForKey: @"approveTime"];
        if(self.approveTime && [self.approveTime isEqual:[NSNull null]])
            self.approveTime = nil;

        self.phonePrivateFlag = [jsonObject objectForKey: @"phonePrivateFlag"];
        if(self.phonePrivateFlag && [self.phonePrivateFlag isEqual:[NSNull null]])
            self.phonePrivateFlag = nil;

        self.cellPhone = [jsonObject objectForKey: @"cellPhone"];
        if(self.cellPhone && [self.cellPhone isEqual:[NSNull null]])
            self.cellPhone = nil;

        self.muteNotificationFlag = [jsonObject objectForKey: @"muteNotificationFlag"];
        if(self.muteNotificationFlag && [self.muteNotificationFlag isEqual:[NSNull null]])
            self.muteNotificationFlag = nil;

        self.addressId = [jsonObject objectForKey: @"addressId"];
        if(self.addressId && [self.addressId isEqual:[NSNull null]])
            self.addressId = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.apartmentName = [jsonObject objectForKey: @"apartmentName"];
        if(self.apartmentName && [self.apartmentName isEqual:[NSNull null]])
            self.apartmentName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
