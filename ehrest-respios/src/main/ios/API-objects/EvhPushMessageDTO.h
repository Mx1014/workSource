//
// EvhPushMessageDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPushMessageDTO
//
@interface EvhPushMessageDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* messageType;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSNumber* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* finishTime;

@property(nonatomic, copy) NSString* deviceType;

@property(nonatomic, copy) NSString* deviceTag;

@property(nonatomic, copy) NSString* appVersion;

@property(nonatomic, copy) NSNumber* pushCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

