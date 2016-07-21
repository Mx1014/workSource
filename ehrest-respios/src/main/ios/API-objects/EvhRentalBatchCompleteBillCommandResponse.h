//
// EvhRentalBatchCompleteBillCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalBatchCompleteBillCommandResponse
//
@interface EvhRentalBatchCompleteBillCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalRentalBillDTO*
@property(nonatomic, strong) NSMutableArray* bills;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

