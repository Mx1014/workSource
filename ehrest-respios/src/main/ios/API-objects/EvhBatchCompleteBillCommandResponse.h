//
// EvhBatchCompleteBillCommandResponse.h
// generated at 2016-03-25 17:08:10 
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

