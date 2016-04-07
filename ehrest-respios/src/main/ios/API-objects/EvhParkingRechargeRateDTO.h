//
// EvhParkingRechargeRateDTO.h
// generated at 2016-04-07 15:16:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingRechargeRateDTO
//
@interface EvhParkingRechargeRateDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* parkingLotId;

@property(nonatomic, copy) NSString* vendorName;

@property(nonatomic, copy) NSString* rateToken;

@property(nonatomic, copy) NSString* rateName;

@property(nonatomic, copy) NSNumber* monthCount;

@property(nonatomic, copy) NSNumber* price;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

