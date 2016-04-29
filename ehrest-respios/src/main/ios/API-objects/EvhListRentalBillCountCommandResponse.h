//
// EvhListRentalBillCountCommandResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
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

