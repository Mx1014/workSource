//
// EvhBatchCompleteBillCommandResponse.h
// generated at 2016-04-08 20:09:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBatchCompleteBillCommandResponse
//
@interface EvhBatchCompleteBillCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalBillDTO*
@property(nonatomic, strong) NSMutableArray* bills;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

