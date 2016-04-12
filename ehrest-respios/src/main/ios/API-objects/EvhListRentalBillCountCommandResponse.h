//
// EvhListRentalBillCountCommandResponse.h
// generated at 2016-04-12 19:00:51 
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

