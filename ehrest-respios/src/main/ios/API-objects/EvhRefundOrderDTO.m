//
// EvhRefundOrderDTO.m
//
#import "EvhRefundOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRefundOrderDTO
//

@implementation EvhRefundOrderDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRefundOrderDTO* obj = [EvhRefundOrderDTO new];
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
    if(self.rentalBillId)
        [jsonObject setObject: self.rentalBillId forKey: @"rentalBillId"];
    if(self.refundOrderNo)
        [jsonObject setObject: self.refundOrderNo forKey: @"refundOrderNo"];
    if(self.launchPadItemId)
        [jsonObject setObject: self.launchPadItemId forKey: @"launchPadItemId"];
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
    if(self.vendorType)
        [jsonObject setObject: self.vendorType forKey: @"vendorType"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.applyUserName)
        [jsonObject setObject: self.applyUserName forKey: @"applyUserName"];
    if(self.applyUserContact)
        [jsonObject setObject: self.applyUserContact forKey: @"applyUserContact"];
    if(self.applyTime)
        [jsonObject setObject: self.applyTime forKey: @"applyTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.rentalBillId = [jsonObject objectForKey: @"rentalBillId"];
        if(self.rentalBillId && [self.rentalBillId isEqual:[NSNull null]])
            self.rentalBillId = nil;

        self.refundOrderNo = [jsonObject objectForKey: @"refundOrderNo"];
        if(self.refundOrderNo && [self.refundOrderNo isEqual:[NSNull null]])
            self.refundOrderNo = nil;

        self.launchPadItemId = [jsonObject objectForKey: @"launchPadItemId"];
        if(self.launchPadItemId && [self.launchPadItemId isEqual:[NSNull null]])
            self.launchPadItemId = nil;

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        self.vendorType = [jsonObject objectForKey: @"vendorType"];
        if(self.vendorType && [self.vendorType isEqual:[NSNull null]])
            self.vendorType = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.applyUserName = [jsonObject objectForKey: @"applyUserName"];
        if(self.applyUserName && [self.applyUserName isEqual:[NSNull null]])
            self.applyUserName = nil;

        self.applyUserContact = [jsonObject objectForKey: @"applyUserContact"];
        if(self.applyUserContact && [self.applyUserContact isEqual:[NSNull null]])
            self.applyUserContact = nil;

        self.applyTime = [jsonObject objectForKey: @"applyTime"];
        if(self.applyTime && [self.applyTime isEqual:[NSNull null]])
            self.applyTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
