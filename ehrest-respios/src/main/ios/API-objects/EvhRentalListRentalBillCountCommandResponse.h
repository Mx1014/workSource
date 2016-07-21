//
// EvhRentalListRentalBillCountCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalRentalBillCountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalListRentalBillCountCommandResponse
//
@interface EvhRentalListRentalBillCountCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalRentalBillCountDTO*
@property(nonatomic, strong) NSMutableArray* rentalBillCounts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

