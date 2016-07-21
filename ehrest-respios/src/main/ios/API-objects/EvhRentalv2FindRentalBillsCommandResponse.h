//
// EvhRentalv2FindRentalBillsCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2FindRentalBillsCommandResponse
//
@interface EvhRentalv2FindRentalBillsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhRentalBillDTO*
@property(nonatomic, strong) NSMutableArray* rentalBills;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

