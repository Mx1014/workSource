//
// EvhRecommendConfigDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendConfigDTO
//
@interface EvhRecommendConfigDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* appid;

@property(nonatomic, copy) NSNumber* suggestType;

@property(nonatomic, copy) NSNumber* sourceType;

@property(nonatomic, copy) NSNumber* sourceId;

@property(nonatomic, copy) NSNumber* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSNumber* periodType;

@property(nonatomic, copy) NSNumber* periodValue;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* runningTime;

@property(nonatomic, copy) NSNumber* expireTime;

@property(nonatomic, copy) NSString* embeddedJson;

@property(nonatomic, copy) NSString* description_;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

