//
// EvhOpPromotionMessageDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionMessageDTO
//
@interface EvhOpPromotionMessageDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* messageSeq;

@property(nonatomic, copy) NSNumber* metaAppId;

@property(nonatomic, copy) NSString* resultData;

@property(nonatomic, copy) NSString* messageText;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* messageMeta;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* senderUid;

@property(nonatomic, copy) NSNumber* targetUid;

@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

