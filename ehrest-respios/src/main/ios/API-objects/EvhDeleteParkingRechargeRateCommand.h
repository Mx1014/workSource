//
// EvhDeleteParkingRechargeRateCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteParkingRechargeRateCommand
//
@interface EvhDeleteParkingRechargeRateCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* parkingLotId;

@property(nonatomic, copy) NSString* vendorName;

@property(nonatomic, copy) NSString* rateToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

