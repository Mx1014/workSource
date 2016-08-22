//
// EvhListRentalBillsCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2RentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRentalBillsCommandResponse
//
@interface EvhListRentalBillsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhRentalv2RentalBillDTO*
@property(nonatomic, strong) NSMutableArray* rentalBills;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

