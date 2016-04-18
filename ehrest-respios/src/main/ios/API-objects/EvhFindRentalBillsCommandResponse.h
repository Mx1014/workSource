//
// EvhFindRentalBillsCommandResponse.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalBillsCommandResponse
//
@interface EvhFindRentalBillsCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalBillDTO*
@property(nonatomic, strong) NSMutableArray* rentalBills;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

