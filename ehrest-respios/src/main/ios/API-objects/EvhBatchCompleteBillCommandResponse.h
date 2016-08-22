//
// EvhBatchCompleteBillCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2RentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBatchCompleteBillCommandResponse
//
@interface EvhBatchCompleteBillCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalv2RentalBillDTO*
@property(nonatomic, strong) NSMutableArray* bills;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

