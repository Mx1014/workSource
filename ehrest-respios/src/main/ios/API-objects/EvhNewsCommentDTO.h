//
// EvhNewsCommentDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhNewsAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsCommentDTO
//
@interface EvhNewsCommentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* Id;

@property(nonatomic, copy) NSString* theNewsToken;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSString* creatorNickName;

@property(nonatomic, copy) NSString* creatorAvatar;

@property(nonatomic, copy) NSString* creatorAvatarUrl;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSNumber* createTime;

// item type EvhNewsAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

@property(nonatomic, copy) NSNumber* deleteFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

