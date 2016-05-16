//
// EvhRechargeInfoDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRechargeInfoDTO
//
@interface EvhRechargeInfoDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* billId;

@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSNumber* numberType;

@property(nonatomic, copy) NSString* ownerName;

@property(nonatomic, copy) NSNumber* rechargeUserid;

@property(nonatomic, copy) NSString* rechargeUsername;

@property(nonatomic, copy) NSString* rechargePhone;

@property(nonatomic, copy) NSNumber* rechargeTime;

@property(nonatomic, copy) NSNumber* rechargeMonth;

@property(nonatomic, copy) NSNumber* rechargeAmount;

@property(nonatomic, copy) NSNumber* oldValidityperiod;

@property(nonatomic, copy) NSNumber* theNewValidityperiod;

@property(nonatomic, copy) NSNumber* paymentStatus;

@property(nonatomic, copy) NSNumber* rechargeStatus;

@property(nonatomic, copy) NSNumber* enterpriseCommunityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

