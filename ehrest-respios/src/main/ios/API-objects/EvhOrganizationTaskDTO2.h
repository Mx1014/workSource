//
// EvhOrganizationTaskDTO2.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhForumAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationTaskDTO2
//
@interface EvhOrganizationTaskDTO2
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* taskId;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* organizationType;

@property(nonatomic, copy) NSString* applyEntityType;

@property(nonatomic, copy) NSNumber* applyEntityId;

@property(nonatomic, copy) NSString* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSString* taskType;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* taskStatus;

@property(nonatomic, copy) NSNumber* operatorUid;

@property(nonatomic, copy) NSNumber* operateTime;

@property(nonatomic, copy) NSNumber* taskCreatorUid;

@property(nonatomic, copy) NSNumber* taskCreateTime;

@property(nonatomic, copy) NSNumber* assignStatus;

@property(nonatomic, copy) NSNumber* unprocessedTime;

@property(nonatomic, copy) NSNumber* processingTime;

@property(nonatomic, copy) NSNumber* processedTime;

@property(nonatomic, copy) NSString* taskCategory;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, copy) NSNumber* parentPostId;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSString* creatorNickName;

@property(nonatomic, copy) NSString* creatorAvatar;

@property(nonatomic, copy) NSString* creatorAvatarUrl;

@property(nonatomic, copy) NSNumber* creatorAdminFlag;

@property(nonatomic, copy) NSString* creatorTag;

@property(nonatomic, copy) NSString* targetTag;

@property(nonatomic, copy) NSNumber* contentCategory;

@property(nonatomic, copy) NSNumber* actionCategory;

@property(nonatomic, copy) NSNumber* visibleRegionType;

@property(nonatomic, copy) NSNumber* visibleRegionId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSNumber* embeddedAppId;

@property(nonatomic, copy) NSNumber* embeddedId;

@property(nonatomic, copy) NSString* embeddedJson;

@property(nonatomic, copy) NSNumber* isForwarded;

@property(nonatomic, copy) NSNumber* childCount;

@property(nonatomic, copy) NSNumber* forwardCount;

@property(nonatomic, copy) NSNumber* likeCount;

@property(nonatomic, copy) NSNumber* dislikeCount;

@property(nonatomic, copy) NSNumber* viewCount;

@property(nonatomic, copy) NSNumber* updateTime;

@property(nonatomic, copy) NSNumber* createTime;

// item type EvhForumAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

@property(nonatomic, copy) NSNumber* assignedFlag;

@property(nonatomic, copy) NSString* forumName;

@property(nonatomic, copy) NSNumber* likeFlag;

@property(nonatomic, copy) NSString* shareUrl;

@property(nonatomic, copy) NSNumber* privateFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

