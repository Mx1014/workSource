//
// EvhParkingChargeRule.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingChargeRule
//
@interface EvhParkingChargeRule
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* month;

@property(nonatomic, copy) NSNumber* amount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

