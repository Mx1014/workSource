//
// EvhListRentalBillCountCommandResponse.h
// generated at 2016-04-18 14:48:50 
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

