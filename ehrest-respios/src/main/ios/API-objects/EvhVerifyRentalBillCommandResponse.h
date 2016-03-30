//
// EvhVerifyRentalBillCommandResponse.h
// generated at 2016-03-30 10:13:08 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyRentalBillCommandResponse
//
@interface EvhVerifyRentalBillCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* addBillCode;

@property(nonatomic, strong) EvhRentalBillDTO* rentalBill;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

