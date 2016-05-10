//
// EvhOweFamilyDTO.m
//
#import "EvhOweFamilyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOweFamilyDTO
//

@implementation EvhOweFamilyDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOweFamilyDTO* obj = [EvhOweFamilyDTO new];
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
    if(self.oweAmount)
        [jsonObject setObject: self.oweAmount forKey: @"oweAmount"];
    if(self.billDescription)
        [jsonObject setObject: self.billDescription forKey: @"billDescription"];
    if(self.billId)
        [jsonObject setObject: self.billId forKey: @"billId"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.addressId)
        [jsonObject setObject: self.addressId forKey: @"addressId"];
    if(self.lastPayTime)
        [jsonObject setObject: self.lastPayTime forKey: @"lastPayTime"];
    if(self.lastBillingTransactionId)
        [jsonObject setObject: self.lastBillingTransactionId forKey: @"lastBillingTransactionId"];
    if(self.ownerTelephone)
        [jsonObject setObject: self.ownerTelephone forKey: @"ownerTelephone"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.oweAmount = [jsonObject objectForKey: @"oweAmount"];
        if(self.oweAmount && [self.oweAmount isEqual:[NSNull null]])
            self.oweAmount = nil;

        self.billDescription = [jsonObject objectForKey: @"billDescription"];
        if(self.billDescription && [self.billDescription isEqual:[NSNull null]])
            self.billDescription = nil;

        self.billId = [jsonObject objectForKey: @"billId"];
        if(self.billId && [self.billId isEqual:[NSNull null]])
            self.billId = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.addressId = [jsonObject objectForKey: @"addressId"];
        if(self.addressId && [self.addressId isEqual:[NSNull null]])
            self.addressId = nil;

        self.lastPayTime = [jsonObject objectForKey: @"lastPayTime"];
        if(self.lastPayTime && [self.lastPayTime isEqual:[NSNull null]])
            self.lastPayTime = nil;

        self.lastBillingTransactionId = [jsonObject objectForKey: @"lastBillingTransactionId"];
        if(self.lastBillingTransactionId && [self.lastBillingTransactionId isEqual:[NSNull null]])
            self.lastBillingTransactionId = nil;

        self.ownerTelephone = [jsonObject objectForKey: @"ownerTelephone"];
        if(self.ownerTelephone && [self.ownerTelephone isEqual:[NSNull null]])
            self.ownerTelephone = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
