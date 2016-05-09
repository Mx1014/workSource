//
// EvhCreateParkingRechargeRateCommand.h
// generated at 2016-04-29 18:56:03 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateParkingRechargeRateCommand
//
@interface EvhCreateParkingRechargeRateCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* parkingLotId;

@property(nonatomic, copy) NSString* rateName;

@property(nonatomic, copy) NSNumber* monthCount;

@property(nonatomic, copy) NSNumber* price;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

