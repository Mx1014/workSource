//
// EvhRentalv2ListRentalBillCountCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2RentalBillCountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2ListRentalBillCountCommandResponse
//
@interface EvhRentalv2ListRentalBillCountCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalv2RentalBillCountDTO*
@property(nonatomic, strong) NSMutableArray* rentalBillCounts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

