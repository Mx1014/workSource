//
// EvhParkingRechargeOrderDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingRechargeOrderDTO
//
@interface EvhParkingRechargeOrderDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* parkingLotId;

@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSString* plateOwnerName;

@property(nonatomic, copy) NSString* plateOwnerPhone;

@property(nonatomic, copy) NSNumber* payerEnterpriseId;

@property(nonatomic, copy) NSNumber* payerUid;

@property(nonatomic, copy) NSString* payerName;

@property(nonatomic, copy) NSString* payerPhone;

@property(nonatomic, copy) NSString* paidType;

@property(nonatomic, copy) NSNumber* paidTime;

@property(nonatomic, copy) NSString* vendorName;

@property(nonatomic, copy) NSString* cardNumber;

@property(nonatomic, copy) NSString* rateToken;

@property(nonatomic, copy) NSString* rateName;

@property(nonatomic, copy) NSNumber* monthCount;

@property(nonatomic, copy) NSNumber* price;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* rechargeStatus;

@property(nonatomic, copy) NSNumber* rechargeTime;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

