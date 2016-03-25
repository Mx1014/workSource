//
// EvhUseCouponActionData.h
// generated at 2016-03-25 15:57:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUseCouponActionData
//
@interface EvhUseCouponActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* couponId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

