//
// EvhParkResponseList.h
// generated at 2016-04-12 15:02:19 
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

