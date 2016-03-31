//
// EvhOrganizationBillingTransactionDTO.h
// generated at 2016-03-31 19:08:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationBillingTransactionDTO
//
@interface EvhOrganizationBillingTransactionDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* txType;

@property(nonatomic, copy) NSNumber* chargeAmount;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* vendor;

@property(nonatomic, copy) NSNumber* paidType;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* ownerTelephone;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSString* address;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

