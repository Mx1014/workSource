//
// EvhListParkingRechargeOrdersResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhParkingRechargeOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListParkingRechargeOrdersResponse
//
@interface EvhListParkingRechargeOrdersResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhParkingRechargeOrderDTO*
@property(nonatomic, strong) NSMutableArray* orders;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

