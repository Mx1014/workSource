//
// EvhCreateRecommendConfig.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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

