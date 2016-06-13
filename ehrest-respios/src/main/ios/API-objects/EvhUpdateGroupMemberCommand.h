//
// EvhUpdateGroupMemberCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateGroupMemberCommand
//
@interface EvhUpdateGroupMemberCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSNumber* memberId;

@property(nonatomic, copy) NSString* memberNickName;

@property(nonatomic, copy) NSString* memberAvatar;

@property(nonatomic, copy) NSNumber* phonePrivateFlag;

@property(nonatomic, copy) NSNumber* muteNotificationFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

