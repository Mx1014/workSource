//
// EvhOrganizationBillingTransactionDTO.m
//
#import "EvhOrganizationBillingTransactionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationBillingTransactionDTO
//

@implementation EvhOrganizationBillingTransactionDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationBillingTransactionDTO* obj = [EvhOrganizationBillingTransactionDTO new];
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
    if(self.txType)
        [jsonObject setObject: self.txType forKey: @"txType"];
    if(self.chargeAmount)
        [jsonObject setObject: self.chargeAmount forKey: @"chargeAmount"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.vendor)
        [jsonObject setObject: self.vendor forKey: @"vendor"];
    if(self.paidType)
        [jsonObject setObject: self.paidType forKey: @"paidType"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.ownerTelephone)
        [jsonObject setObject: self.ownerTelephone forKey: @"ownerTelephone"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.apartmentId)
        [jsonObject setObject: self.apartmentId forKey: @"apartmentId"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.txType = [jsonObject objectForKey: @"txType"];
        if(self.txType && [self.txType isEqual:[NSNull null]])
            self.txType = nil;

        self.chargeAmount = [jsonObject objectForKey: @"chargeAmount"];
        if(self.chargeAmount && [self.chargeAmount isEqual:[NSNull null]])
            self.chargeAmount = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.vendor = [jsonObject objectForKey: @"vendor"];
        if(self.vendor && [self.vendor isEqual:[NSNull null]])
            self.vendor = nil;

        self.paidType = [jsonObject objectForKey: @"paidType"];
        if(self.paidType && [self.paidType isEqual:[NSNull null]])
            self.paidType = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.ownerTelephone = [jsonObject objectForKey: @"ownerTelephone"];
        if(self.ownerTelephone && [self.ownerTelephone isEqual:[NSNull null]])
            self.ownerTelephone = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.apartmentId = [jsonObject objectForKey: @"apartmentId"];
        if(self.apartmentId && [self.apartmentId isEqual:[NSNull null]])
            self.apartmentId = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
