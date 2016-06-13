//
// EvhPayPmBillByAddressIdCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPayPmBillByAddressIdCommand
//
@interface EvhPayPmBillByAddressIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSNumber* payTime;

@property(nonatomic, copy) NSNumber* payAmount;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* ownerName;

@property(nonatomic, copy) NSString* telephone;

@property(nonatomic, copy) NSString* vendor;

@property(nonatomic, copy) NSNumber* txType;

@property(nonatomic, copy) NSNumber* paidType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

