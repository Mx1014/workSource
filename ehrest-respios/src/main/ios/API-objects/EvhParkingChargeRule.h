//
// EvhParkingChargeRule.h
// generated at 2016-04-08 20:09:21 
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

