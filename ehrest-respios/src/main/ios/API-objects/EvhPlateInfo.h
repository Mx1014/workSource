//
// EvhPlateInfo.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
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

