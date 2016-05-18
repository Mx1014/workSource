//
// EvhListTreasureResponse.h
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

