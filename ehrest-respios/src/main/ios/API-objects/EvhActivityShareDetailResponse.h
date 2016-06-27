//
// EvhActivityShareDetailResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhActivityDTO.h"
#import "EvhAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityShareDetailResponse
//
@interface EvhActivityShareDetailResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhActivityDTO* activity;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSNumber* childCount;

@property(nonatomic, copy) NSNumber* viewCount;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* creatorNickName;

@property(nonatomic, copy) NSNumber* createTime;

// item type EvhAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

@property(nonatomic, copy) NSString* creatorAvatarUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

