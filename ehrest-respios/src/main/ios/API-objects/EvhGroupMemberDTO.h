//
// EvhGroupMemberDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupMemberDTO
//
@interface EvhGroupMemberDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSString* groupName;

@property(nonatomic, copy) NSString* memberType;

@property(nonatomic, copy) NSNumber* memberId;

@property(nonatomic, copy) NSNumber* memberRole;

@property(nonatomic, copy) NSString* memberNickName;

@property(nonatomic, copy) NSString* memberAvatar;

@property(nonatomic, copy) NSString* memberAvatarUrl;

@property(nonatomic, copy) NSNumber* memberStatus;

@property(nonatomic, copy) NSNumber* inviterUid;

@property(nonatomic, copy) NSString* inviterNickName;

@property(nonatomic, copy) NSString* inviterAvatar;

@property(nonatomic, copy) NSString* inviterAvatarUrl;

@property(nonatomic, copy) NSNumber* inviteTime;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* approveTime;

@property(nonatomic, copy) NSNumber* phonePrivateFlag;

@property(nonatomic, copy) NSString* cellPhone;

@property(nonatomic, copy) NSNumber* muteNotificationFlag;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* apartmentName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

