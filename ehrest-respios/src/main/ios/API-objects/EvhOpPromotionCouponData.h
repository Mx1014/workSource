//
// EvhOpPromotionCouponData.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionCouponData
//
@interface EvhOpPromotionCouponData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* url;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* couponList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

