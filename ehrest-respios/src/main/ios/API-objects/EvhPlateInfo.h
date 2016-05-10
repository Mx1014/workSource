//
// EvhPlateInfo.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhParkingChargeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPlateInfo
//
@interface EvhPlateInfo
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSString* ownerName;

@property(nonatomic, copy) NSNumber* validityPeriod;

@property(nonatomic, copy) NSString* cardType;

@property(nonatomic, copy) NSString* cardCode;

@property(nonatomic, copy) NSString* isValid;

// item type EvhParkingChargeDTO*
@property(nonatomic, strong) NSMutableArray* parkingCharge;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

