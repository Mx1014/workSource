//
// EvhListRentalBillCountCommandResponse.h
// generated at 2016-03-31 15:43:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalBillCountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRentalBillCountCommandResponse
//
@interface EvhListRentalBillCountCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalBillCountDTO*
@property(nonatomic, strong) NSMutableArray* rentalBillCounts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

