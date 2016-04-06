//
// EvhRechargeRecordDTO.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRechargeRecordDTO
//
@interface EvhRechargeRecordDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSString* ownerName;

@property(nonatomic, copy) NSString* rechargePhone;

@property(nonatomic, copy) NSNumber* rechargeTime;

@property(nonatomic, copy) NSNumber* rechargeMonth;

@property(nonatomic, copy) NSNumber* rechargeAmount;

@property(nonatomic, copy) NSNumber* paymentStatus;

@property(nonatomic, copy) NSNumber* rechargeStatus;

@property(nonatomic, copy) NSNumber* validityperiod;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

