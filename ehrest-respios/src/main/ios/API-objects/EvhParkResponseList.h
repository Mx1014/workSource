//
// EvhParkResponseList.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhParkingChargeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkResponseList
//
@interface EvhParkResponseList
    : NSObject<EvhJsonSerializable>


// item type EvhParkingChargeDTO*
@property(nonatomic, strong) NSMutableArray* parkingCharge;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

