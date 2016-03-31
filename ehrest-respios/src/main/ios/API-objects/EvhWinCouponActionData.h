//
// EvhWinCouponActionData.h
// generated at 2016-03-31 15:43:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWinCouponActionData
//
@interface EvhWinCouponActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* couponId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

