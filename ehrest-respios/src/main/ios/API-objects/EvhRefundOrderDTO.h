//
// EvhRefundOrderDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRefundOrderDTO
//
@interface EvhRefundOrderDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* rentalBillId;

@property(nonatomic, copy) NSNumber* refundOrderNo;

@property(nonatomic, copy) NSNumber* resourceTypeId;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSString* vendorType;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* applyUserName;

@property(nonatomic, copy) NSString* applyUserContact;

@property(nonatomic, copy) NSNumber* applyTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

