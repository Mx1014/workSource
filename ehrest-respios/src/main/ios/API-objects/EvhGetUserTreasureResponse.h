//
// EvhGetUserTreasureResponse.h
// generated at 2016-04-08 20:09:21 
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

