//
// EvhLinkDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLinkDTO
//
@interface EvhLinkDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* ownerUid;

@property(nonatomic, copy) NSNumber* sourceType;

@property(nonatomic, copy) NSNumber* sourceId;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSString* author;

@property(nonatomic, copy) NSString* coverUri;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSString* contentAbstract;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* deleterUid;

@property(nonatomic, copy) NSNumber* deleteTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

