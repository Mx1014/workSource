//
// EvhCreateInvoiceCommand.m
//
#import "EvhCreateInvoiceCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateInvoiceCommand
//

@implementation EvhCreateInvoiceCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateInvoiceCommand* obj = [EvhCreateInvoiceCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.orderId)
        [jsonObject setObject: self.orderId forKey: @"orderId"];
    if(self.taxpayerType)
        [jsonObject setObject: self.taxpayerType forKey: @"taxpayerType"];
    if(self.vatType)
        [jsonObject setObject: self.vatType forKey: @"vatType"];
    if(self.expenseType)
        [jsonObject setObject: self.expenseType forKey: @"expenseType"];
    if(self.companyName)
        [jsonObject setObject: self.companyName forKey: @"companyName"];
    if(self.taxRegCertificateNum)
        [jsonObject setObject: self.taxRegCertificateNum forKey: @"taxRegCertificateNum"];
    if(self.taxRegAddress)
        [jsonObject setObject: self.taxRegAddress forKey: @"taxRegAddress"];
    if(self.taxRegPhone)
        [jsonObject setObject: self.taxRegPhone forKey: @"taxRegPhone"];
    if(self.bankName)
        [jsonObject setObject: self.bankName forKey: @"bankName"];
    if(self.bankAccount)
        [jsonObject setObject: self.bankAccount forKey: @"bankAccount"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.zipCode)
        [jsonObject setObject: self.zipCode forKey: @"zipCode"];
    if(self.consignee)
        [jsonObject setObject: self.consignee forKey: @"consignee"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
    if(self.contractFlag)
        [jsonObject setObject: self.contractFlag forKey: @"contractFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.orderId = [jsonObject objectForKey: @"orderId"];
        if(self.orderId && [self.orderId isEqual:[NSNull null]])
            self.orderId = nil;

        self.taxpayerType = [jsonObject objectForKey: @"taxpayerType"];
        if(self.taxpayerType && [self.taxpayerType isEqual:[NSNull null]])
            self.taxpayerType = nil;

        self.vatType = [jsonObject objectForKey: @"vatType"];
        if(self.vatType && [self.vatType isEqual:[NSNull null]])
            self.vatType = nil;

        self.expenseType = [jsonObject objectForKey: @"expenseType"];
        if(self.expenseType && [self.expenseType isEqual:[NSNull null]])
            self.expenseType = nil;

        self.companyName = [jsonObject objectForKey: @"companyName"];
        if(self.companyName && [self.companyName isEqual:[NSNull null]])
            self.companyName = nil;

        self.taxRegCertificateNum = [jsonObject objectForKey: @"taxRegCertificateNum"];
        if(self.taxRegCertificateNum && [self.taxRegCertificateNum isEqual:[NSNull null]])
            self.taxRegCertificateNum = nil;

        self.taxRegAddress = [jsonObject objectForKey: @"taxRegAddress"];
        if(self.taxRegAddress && [self.taxRegAddress isEqual:[NSNull null]])
            self.taxRegAddress = nil;

        self.taxRegPhone = [jsonObject objectForKey: @"taxRegPhone"];
        if(self.taxRegPhone && [self.taxRegPhone isEqual:[NSNull null]])
            self.taxRegPhone = nil;

        self.bankName = [jsonObject objectForKey: @"bankName"];
        if(self.bankName && [self.bankName isEqual:[NSNull null]])
            self.bankName = nil;

        self.bankAccount = [jsonObject objectForKey: @"bankAccount"];
        if(self.bankAccount && [self.bankAccount isEqual:[NSNull null]])
            self.bankAccount = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.zipCode = [jsonObject objectForKey: @"zipCode"];
        if(self.zipCode && [self.zipCode isEqual:[NSNull null]])
            self.zipCode = nil;

        self.consignee = [jsonObject objectForKey: @"consignee"];
        if(self.consignee && [self.consignee isEqual:[NSNull null]])
            self.consignee = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        self.contractFlag = [jsonObject objectForKey: @"contractFlag"];
        if(self.contractFlag && [self.contractFlag isEqual:[NSNull null]])
            self.contractFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
