//
// EvhCardTransactionFromVendorDTO.m
//
#import "EvhCardTransactionFromVendorDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCardTransactionFromVendorDTO
//

@implementation EvhCardTransactionFromVendorDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCardTransactionFromVendorDTO* obj = [EvhCardTransactionFromVendorDTO new];
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
    if(self.itemName)
        [jsonObject setObject: self.itemName forKey: @"itemName"];
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
    if(self.transactionTime)
        [jsonObject setObject: self.transactionTime forKey: @"transactionTime"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.merchant)
        [jsonObject setObject: self.merchant forKey: @"merchant"];
    if(self.transactionType)
        [jsonObject setObject: self.transactionType forKey: @"transactionType"];
    if(self.vendorName)
        [jsonObject setObject: self.vendorName forKey: @"vendorName"];
    if(self.vendorResult)
        [jsonObject setObject: self.vendorResult forKey: @"vendorResult"];
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.itemName = [jsonObject objectForKey: @"itemName"];
        if(self.itemName && [self.itemName isEqual:[NSNull null]])
            self.itemName = nil;

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        self.transactionTime = [jsonObject objectForKey: @"transactionTime"];
        if(self.transactionTime && [self.transactionTime isEqual:[NSNull null]])
            self.transactionTime = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.merchant = [jsonObject objectForKey: @"merchant"];
        if(self.merchant && [self.merchant isEqual:[NSNull null]])
            self.merchant = nil;

        self.transactionType = [jsonObject objectForKey: @"transactionType"];
        if(self.transactionType && [self.transactionType isEqual:[NSNull null]])
            self.transactionType = nil;

        self.vendorName = [jsonObject objectForKey: @"vendorName"];
        if(self.vendorName && [self.vendorName isEqual:[NSNull null]])
            self.vendorName = nil;

        self.vendorResult = [jsonObject objectForKey: @"vendorResult"];
        if(self.vendorResult && [self.vendorResult isEqual:[NSNull null]])
            self.vendorResult = nil;

        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
