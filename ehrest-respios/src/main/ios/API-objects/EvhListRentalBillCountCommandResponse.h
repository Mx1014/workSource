//
// EvhListRentalBillCountCommandResponse.h
// generated at 2016-04-19 12:41:53 
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

