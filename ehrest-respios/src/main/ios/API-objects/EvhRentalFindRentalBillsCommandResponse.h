//
// EvhRentalFindRentalBillsCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindRentalBillsCommandResponse
//
@interface EvhRentalFindRentalBillsCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalRentalBillDTO*
@property(nonatomic, strong) NSMutableArray* rentalBills;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

