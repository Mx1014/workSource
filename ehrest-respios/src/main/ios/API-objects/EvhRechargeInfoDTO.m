//
// EvhRechargeInfoDTO.m
//
#import "EvhRechargeInfoDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRechargeInfoDTO
//

@implementation EvhRechargeInfoDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRechargeInfoDTO* obj = [EvhRechargeInfoDTO new];
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
    if(self.billId)
        [jsonObject setObject: self.billId forKey: @"billId"];
    if(self.plateNumber)
        [jsonObject setObject: self.plateNumber forKey: @"plateNumber"];
    if(self.numberType)
        [jsonObject setObject: self.numberType forKey: @"numberType"];
    if(self.ownerName)
        [jsonObject setObject: self.ownerName forKey: @"ownerName"];
    if(self.rechargeUserid)
        [jsonObject setObject: self.rechargeUserid forKey: @"rechargeUserid"];
    if(self.rechargeUsername)
        [jsonObject setObject: self.rechargeUsername forKey: @"rechargeUsername"];
    if(self.rechargePhone)
        [jsonObject setObject: self.rechargePhone forKey: @"rechargePhone"];
    if(self.rechargeTime)
        [jsonObject setObject: self.rechargeTime forKey: @"rechargeTime"];
    if(self.rechargeMonth)
        [jsonObject setObject: self.rechargeMonth forKey: @"rechargeMonth"];
    if(self.rechargeAmount)
        [jsonObject setObject: self.rechargeAmount forKey: @"rechargeAmount"];
    if(self.oldValidityperiod)
        [jsonObject setObject: self.oldValidityperiod forKey: @"oldValidityperiod"];
    if(self.theNewValidityperiod)
        [jsonObject setObject: self.theNewValidityperiod forKey: @"newValidityperiod"];
    if(self.paymentStatus)
        [jsonObject setObject: self.paymentStatus forKey: @"paymentStatus"];
    if(self.rechargeStatus)
        [jsonObject setObject: self.rechargeStatus forKey: @"rechargeStatus"];
    if(self.enterpriseCommunityId)
        [jsonObject setObject: self.enterpriseCommunityId forKey: @"enterpriseCommunityId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.billId = [jsonObject objectForKey: @"billId"];
        if(self.billId && [self.billId isEqual:[NSNull null]])
            self.billId = nil;

        self.plateNumber = [jsonObject objectForKey: @"plateNumber"];
        if(self.plateNumber && [self.plateNumber isEqual:[NSNull null]])
            self.plateNumber = nil;

        self.numberType = [jsonObject objectForKey: @"numberType"];
        if(self.numberType && [self.numberType isEqual:[NSNull null]])
            self.numberType = nil;

        self.ownerName = [jsonObject objectForKey: @"ownerName"];
        if(self.ownerName && [self.ownerName isEqual:[NSNull null]])
            self.ownerName = nil;

        self.rechargeUserid = [jsonObject objectForKey: @"rechargeUserid"];
        if(self.rechargeUserid && [self.rechargeUserid isEqual:[NSNull null]])
            self.rechargeUserid = nil;

        self.rechargeUsername = [jsonObject objectForKey: @"rechargeUsername"];
        if(self.rechargeUsername && [self.rechargeUsername isEqual:[NSNull null]])
            self.rechargeUsername = nil;

        self.rechargePhone = [jsonObject objectForKey: @"rechargePhone"];
        if(self.rechargePhone && [self.rechargePhone isEqual:[NSNull null]])
            self.rechargePhone = nil;

        self.rechargeTime = [jsonObject objectForKey: @"rechargeTime"];
        if(self.rechargeTime && [self.rechargeTime isEqual:[NSNull null]])
            self.rechargeTime = nil;

        self.rechargeMonth = [jsonObject objectForKey: @"rechargeMonth"];
        if(self.rechargeMonth && [self.rechargeMonth isEqual:[NSNull null]])
            self.rechargeMonth = nil;

        self.rechargeAmount = [jsonObject objectForKey: @"rechargeAmount"];
        if(self.rechargeAmount && [self.rechargeAmount isEqual:[NSNull null]])
            self.rechargeAmount = nil;

        self.oldValidityperiod = [jsonObject objectForKey: @"oldValidityperiod"];
        if(self.oldValidityperiod && [self.oldValidityperiod isEqual:[NSNull null]])
            self.oldValidityperiod = nil;

        self.theNewValidityperiod = [jsonObject objectForKey: @"newValidityperiod"];
        if(self.theNewValidityperiod && [self.theNewValidityperiod isEqual:[NSNull null]])
            self.theNewValidityperiod = nil;

        self.paymentStatus = [jsonObject objectForKey: @"paymentStatus"];
        if(self.paymentStatus && [self.paymentStatus isEqual:[NSNull null]])
            self.paymentStatus = nil;

        self.rechargeStatus = [jsonObject objectForKey: @"rechargeStatus"];
        if(self.rechargeStatus && [self.rechargeStatus isEqual:[NSNull null]])
            self.rechargeStatus = nil;

        self.enterpriseCommunityId = [jsonObject objectForKey: @"enterpriseCommunityId"];
        if(self.enterpriseCommunityId && [self.enterpriseCommunityId isEqual:[NSNull null]])
            self.enterpriseCommunityId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
