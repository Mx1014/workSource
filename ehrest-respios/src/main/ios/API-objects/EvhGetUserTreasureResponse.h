//
// EvhGetUserTreasureResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserTreasureResponse
//
@interface EvhGetUserTreasureResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* couponCount;

@property(nonatomic, copy) NSNumber* topicFavoriteCount;

@property(nonatomic, copy) NSNumber* sharedCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

