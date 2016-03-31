//
// EvhWinCouponActionData.h
// generated at 2016-03-28 15:56:07 
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

