//
// EvhCreateInvoiceCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateInvoiceCommand
//
@interface EvhCreateInvoiceCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* orderId;

@property(nonatomic, copy) NSNumber* taxpayerType;

@property(nonatomic, copy) NSNumber* vatType;

@property(nonatomic, copy) NSNumber* expenseType;

@property(nonatomic, copy) NSString* companyName;

@property(nonatomic, copy) NSString* taxRegCertificateNum;

@property(nonatomic, copy) NSString* taxRegAddress;

@property(nonatomic, copy) NSString* taxRegPhone;

@property(nonatomic, copy) NSString* bankName;

@property(nonatomic, copy) NSString* bankAccount;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* zipCode;

@property(nonatomic, copy) NSString* consignee;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSNumber* contractFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

