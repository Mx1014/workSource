//
// EvhGroupDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupDTO
//
@interface EvhGroupDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, copy) NSNumber* owningForumId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* privateFlag;

@property(nonatomic, copy) NSNumber* joinPolicy;

@property(nonatomic, copy) NSNumber* memberCount;

@property(nonatomic, copy) NSString* tag;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSString* categoryName;

@property(nonatomic, copy) NSNumber* memberOf;

@property(nonatomic, copy) NSNumber* memberStatus;

@property(nonatomic, copy) NSString* memberNickName;

@property(nonatomic, copy) NSNumber* memberRole;

@property(nonatomic, copy) NSNumber* phonePrivateFlag;

@property(nonatomic, copy) NSNumber* muteNotificationFlag;

@property(nonatomic, copy) NSNumber* postFlag;

@property(nonatomic, copy) NSString* creatorName;

@property(nonatomic, copy) NSString* creatorFamilyName;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* memberGroupPrivileges;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* memberForumPrivileges;

@property(nonatomic, copy) NSNumber* updateTime;

@property(nonatomic, copy) NSString* discriminator;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

