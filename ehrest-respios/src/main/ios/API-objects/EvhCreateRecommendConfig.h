//
// EvhCreateRecommendConfig.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateRecommendConfig
//
@interface EvhCreateRecommendConfig
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* appid;

@property(nonatomic, copy) NSNumber* suggestType;

@property(nonatomic, copy) NSNumber* sourceType;

@property(nonatomic, copy) NSNumber* sourceId;

@property(nonatomic, copy) NSNumber* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSNumber* periodType;

@property(nonatomic, copy) NSNumber* periodValue;

@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSString* expireTime;

@property(nonatomic, copy) NSString* embeddedJson;

@property(nonatomic, copy) NSString* description_;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

