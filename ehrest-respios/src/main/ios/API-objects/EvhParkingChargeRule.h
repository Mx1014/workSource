//
// EvhParkingChargeRule.h
// generated at 2016-04-06 19:59:45 
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

