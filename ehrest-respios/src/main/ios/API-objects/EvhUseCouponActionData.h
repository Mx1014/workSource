//
// EvhUseCouponActionData.h
// generated at 2016-04-12 19:00:52 
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

