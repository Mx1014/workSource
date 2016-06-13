//
// EvhCreateConfAccountOrderCommand.m
//
#import "EvhCreateConfAccountOrderCommand.h"
#import "EvhInvoiceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateConfAccountOrderCommand
//

@implementation EvhCreateConfAccountOrderCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateConfAccountOrderCommand* obj = [EvhCreateConfAccountOrderCommand new];
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
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.enterpriseName)
        [jsonObject setObject: self.enterpriseName forKey: @"enterpriseName"];
    if(self.contactor)
        [jsonObject setObject: self.contactor forKey: @"contactor"];
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.buyChannel)
        [jsonObject setObject: self.buyChannel forKey: @"buyChannel"];
    if(self.quantity)
        [jsonObject setObject: self.quantity forKey: @"quantity"];
    if(self.period)
        [jsonObject setObject: self.period forKey: @"period"];
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
    if(self.invoiceFlag)
        [jsonObject setObject: self.invoiceFlag forKey: @"invoiceFlag"];
    if(self.makeOutFlag)
        [jsonObject setObject: self.makeOutFlag forKey: @"makeOutFlag"];
    if(self.invoice) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.invoice toJson: dic];
        
        [jsonObject setObject: dic forKey: @"invoice"];
    }
    if(self.accountCategoryId)
        [jsonObject setObject: self.accountCategoryId forKey: @"accountCategoryId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.enterpriseName = [jsonObject objectForKey: @"enterpriseName"];
        if(self.enterpriseName && [self.enterpriseName isEqual:[NSNull null]])
            self.enterpriseName = nil;

        self.contactor = [jsonObject objectForKey: @"contactor"];
        if(self.contactor && [self.contactor isEqual:[NSNull null]])
            self.contactor = nil;

        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.buyChannel = [jsonObject objectForKey: @"buyChannel"];
        if(self.buyChannel && [self.buyChannel isEqual:[NSNull null]])
            self.buyChannel = nil;

        self.quantity = [jsonObject objectForKey: @"quantity"];
        if(self.quantity && [self.quantity isEqual:[NSNull null]])
            self.quantity = nil;

        self.period = [jsonObject objectForKey: @"period"];
        if(self.period && [self.period isEqual:[NSNull null]])
            self.period = nil;

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        self.invoiceFlag = [jsonObject objectForKey: @"invoiceFlag"];
        if(self.invoiceFlag && [self.invoiceFlag isEqual:[NSNull null]])
            self.invoiceFlag = nil;

        self.makeOutFlag = [jsonObject objectForKey: @"makeOutFlag"];
        if(self.makeOutFlag && [self.makeOutFlag isEqual:[NSNull null]])
            self.makeOutFlag = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"invoice"];

        self.invoice = [EvhInvoiceDTO new];
        self.invoice = [self.invoice fromJson: itemJson];
        self.accountCategoryId = [jsonObject objectForKey: @"accountCategoryId"];
        if(self.accountCategoryId && [self.accountCategoryId isEqual:[NSNull null]])
            self.accountCategoryId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
