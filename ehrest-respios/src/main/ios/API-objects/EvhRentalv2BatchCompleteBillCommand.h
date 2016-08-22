//
// EvhRentalv2BatchCompleteBillCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2BatchCompleteBillCommand
//
@interface EvhRentalv2BatchCompleteBillCommand
    : NSObject<EvhJsonSerializable>


// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* rentalBillIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

