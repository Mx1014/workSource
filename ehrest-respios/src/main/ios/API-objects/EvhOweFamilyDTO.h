//
// EvhOweFamilyDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOweFamilyDTO
//
@interface EvhOweFamilyDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* oweAmount;

@property(nonatomic, copy) NSString* billDescription;

@property(nonatomic, copy) NSNumber* billId;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSNumber* lastPayTime;

@property(nonatomic, copy) NSNumber* lastBillingTransactionId;

@property(nonatomic, copy) NSString* ownerTelephone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

