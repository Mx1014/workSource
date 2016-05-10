//
// EvhUpdateInvoiceCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateInvoiceCommand
//
@interface EvhUpdateInvoiceCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* orderId;

@property(nonatomic, copy) NSNumber* taxpayerType;

@property(nonatomic, copy) NSNumber* vatType;

@property(nonatomic, copy) NSNumber* expenseType;

@property(nonatomic, copy) NSString* companyName;

@property(nonatomic, copy) NSString* vatCode;

@property(nonatomic, copy) NSString* vatAddress;

@property(nonatomic, copy) NSString* vatPhone;

@property(nonatomic, copy) NSString* vatBankname;

@property(nonatomic, copy) NSString* vatBankaccount;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* zipCode;

@property(nonatomic, copy) NSString* consignee;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSNumber* contractFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

