//
// EvhWinCouponActionData.h
// generated at 2016-04-06 19:10:42 
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

