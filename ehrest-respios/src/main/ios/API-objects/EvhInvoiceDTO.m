//
// EvhInvoiceDTO.m
//
#import "EvhInvoiceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInvoiceDTO
//

@implementation EvhInvoiceDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhInvoiceDTO* obj = [EvhInvoiceDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
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
    if(self.vatCode)
        [jsonObject setObject: self.vatCode forKey: @"vatCode"];
    if(self.vatAddress)
        [jsonObject setObject: self.vatAddress forKey: @"vatAddress"];
    if(self.vatPhone)
        [jsonObject setObject: self.vatPhone forKey: @"vatPhone"];
    if(self.vatBankname)
        [jsonObject setObject: self.vatBankname forKey: @"vatBankname"];
    if(self.vatBankaccount)
        [jsonObject setObject: self.vatBankaccount forKey: @"vatBankaccount"];
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
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

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

        self.vatCode = [jsonObject objectForKey: @"vatCode"];
        if(self.vatCode && [self.vatCode isEqual:[NSNull null]])
            self.vatCode = nil;

        self.vatAddress = [jsonObject objectForKey: @"vatAddress"];
        if(self.vatAddress && [self.vatAddress isEqual:[NSNull null]])
            self.vatAddress = nil;

        self.vatPhone = [jsonObject objectForKey: @"vatPhone"];
        if(self.vatPhone && [self.vatPhone isEqual:[NSNull null]])
            self.vatPhone = nil;

        self.vatBankname = [jsonObject objectForKey: @"vatBankname"];
        if(self.vatBankname && [self.vatBankname isEqual:[NSNull null]])
            self.vatBankname = nil;

        self.vatBankaccount = [jsonObject objectForKey: @"vatBankaccount"];
        if(self.vatBankaccount && [self.vatBankaccount isEqual:[NSNull null]])
            self.vatBankaccount = nil;

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
