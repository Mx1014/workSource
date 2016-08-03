//
// EvhRentalVerifyRentalBillCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalVerifyRentalBillCommandResponse
//
@interface EvhRentalVerifyRentalBillCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* addBillCode;

@property(nonatomic, strong) EvhRentalRentalBillDTO* rentalBill;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

