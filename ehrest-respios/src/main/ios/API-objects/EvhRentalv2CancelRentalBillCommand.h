//
// EvhRentalv2CancelRentalBillCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2CancelRentalBillCommand
//
@interface EvhRentalv2CancelRentalBillCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalBillId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

