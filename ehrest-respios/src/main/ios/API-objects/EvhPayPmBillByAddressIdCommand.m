//
// EvhPayPmBillByAddressIdCommand.m
//
#import "EvhPayPmBillByAddressIdCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPayPmBillByAddressIdCommand
//

@implementation EvhPayPmBillByAddressIdCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPayPmBillByAddressIdCommand* obj = [EvhPayPmBillByAddressIdCommand new];
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
    if(self.addressId)
        [jsonObject setObject: self.addressId forKey: @"addressId"];
    if(self.payTime)
        [jsonObject setObject: self.payTime forKey: @"payTime"];
    if(self.payAmount)
        [jsonObject setObject: self.payAmount forKey: @"payAmount"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.ownerName)
        [jsonObject setObject: self.ownerName forKey: @"ownerName"];
    if(self.telephone)
        [jsonObject setObject: self.telephone forKey: @"telephone"];
    if(self.vendor)
        [jsonObject setObject: self.vendor forKey: @"vendor"];
    if(self.txType)
        [jsonObject setObject: self.txType forKey: @"txType"];
    if(self.paidType)
        [jsonObject setObject: self.paidType forKey: @"paidType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.addressId = [jsonObject objectForKey: @"addressId"];
        if(self.addressId && [self.addressId isEqual:[NSNull null]])
            self.addressId = nil;

        self.payTime = [jsonObject objectForKey: @"payTime"];
        if(self.payTime && [self.payTime isEqual:[NSNull null]])
            self.payTime = nil;

        self.payAmount = [jsonObject objectForKey: @"payAmount"];
        if(self.payAmount && [self.payAmount isEqual:[NSNull null]])
            self.payAmount = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.ownerName = [jsonObject objectForKey: @"ownerName"];
        if(self.ownerName && [self.ownerName isEqual:[NSNull null]])
            self.ownerName = nil;

        self.telephone = [jsonObject objectForKey: @"telephone"];
        if(self.telephone && [self.telephone isEqual:[NSNull null]])
            self.telephone = nil;

        self.vendor = [jsonObject objectForKey: @"vendor"];
        if(self.vendor && [self.vendor isEqual:[NSNull null]])
            self.vendor = nil;

        self.txType = [jsonObject objectForKey: @"txType"];
        if(self.txType && [self.txType isEqual:[NSNull null]])
            self.txType = nil;

        self.paidType = [jsonObject objectForKey: @"paidType"];
        if(self.paidType && [self.paidType isEqual:[NSNull null]])
            self.paidType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
