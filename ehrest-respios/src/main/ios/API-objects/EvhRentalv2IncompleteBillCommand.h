//
// EvhRentalv2IncompleteBillCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2IncompleteBillCommand
//
@interface EvhRentalv2IncompleteBillCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalBillId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

