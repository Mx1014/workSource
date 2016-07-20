//
// EvhBuildingDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBuildingAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBuildingDTO
//
@interface EvhBuildingDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* aliasName;

@property(nonatomic, copy) NSNumber* managerUid;

@property(nonatomic, copy) NSString* managerNickName;

@property(nonatomic, copy) NSString* managerAvatar;

@property(nonatomic, copy) NSString* managerAvatarUrl;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* areaSize;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* posterUri;

@property(nonatomic, copy) NSString* posterUrl;

@property(nonatomic, copy) NSNumber* operatorUid;

@property(nonatomic, copy) NSNumber* operateTime;

@property(nonatomic, copy) NSString* operateNickName;

@property(nonatomic, copy) NSString* operateAvatar;

@property(nonatomic, copy) NSString* operateAvatarUrl;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSString* creatorNickName;

@property(nonatomic, copy) NSString* creatorAvatar;

@property(nonatomic, copy) NSString* creatorAvatarUrl;

@property(nonatomic, copy) NSNumber* createTime;

// item type EvhBuildingAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

