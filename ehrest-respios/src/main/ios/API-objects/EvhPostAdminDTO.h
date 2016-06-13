//
// EvhPostAdminDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPostAdminDTO
//
@interface EvhPostAdminDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, copy) NSNumber* parentPostId;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSString* forumName;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSString* creatorNickName;

@property(nonatomic, copy) NSString* creatorPhone;

@property(nonatomic, copy) NSNumber* creatorAdminFlag;

@property(nonatomic, copy) NSString* creatorTag;

@property(nonatomic, copy) NSString* targetTag;

@property(nonatomic, copy) NSNumber* contentCategory;

@property(nonatomic, copy) NSNumber* actionCategory;

@property(nonatomic, copy) NSNumber* visibleRegionType;

@property(nonatomic, copy) NSNumber* visibleRegionId;

@property(nonatomic, copy) NSString* visibleRegionName;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSNumber* embeddedAppId;

@property(nonatomic, copy) NSNumber* embeddedId;

@property(nonatomic, copy) NSNumber* isForwarded;

@property(nonatomic, copy) NSNumber* childCount;

@property(nonatomic, copy) NSNumber* forwardCount;

@property(nonatomic, copy) NSNumber* likeCount;

@property(nonatomic, copy) NSNumber* viewCount;

@property(nonatomic, copy) NSNumber* updateTime;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* assignedFlag;

@property(nonatomic, copy) NSString* creatorAddress;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

