//
// EvhUpdateGroupMemberCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
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

