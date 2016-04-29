//
// EvhGroupMemberSnapshotDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupMemberSnapshotDTO
//
@interface EvhGroupMemberSnapshotDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSString* groupName;

@property(nonatomic, copy) NSString* memberType;

@property(nonatomic, copy) NSNumber* memberId;

@property(nonatomic, copy) NSString* memberNickName;

@property(nonatomic, copy) NSString* memberAvatar;

@property(nonatomic, copy) NSString* memberAvatarUrl;

@property(nonatomic, copy) NSNumber* updateTime;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

