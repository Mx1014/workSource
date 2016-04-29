//
// EvhListTreasureResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
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
// EvhListTreasureResponse
//
@interface EvhListTreasureResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* points;

@property(nonatomic, copy) NSNumber* couponCount;

@property(nonatomic, copy) NSNumber* topicFavoriteCount;

@property(nonatomic, copy) NSNumber* sharedCount;

@property(nonatomic, copy) NSString* pointRuleUrl;

@property(nonatomic, copy) NSNumber* level;

@property(nonatomic, copy) NSString* myCoupon;

@property(nonatomic, copy) NSString* myOrderUrl;

@property(nonatomic, copy) NSString* applyShopUrl;

@property(nonatomic, copy) NSNumber* isAppliedShop;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

