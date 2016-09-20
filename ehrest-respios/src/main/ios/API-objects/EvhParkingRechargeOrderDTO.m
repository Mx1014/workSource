//
// EvhParkingRechargeOrderDTO.m
//
#import "EvhParkingRechargeOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingRechargeOrderDTO
//

@implementation EvhParkingRechargeOrderDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhParkingRechargeOrderDTO* obj = [EvhParkingRechargeOrderDTO new];
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
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.parkingLotId)
        [jsonObject setObject: self.parkingLotId forKey: @"parkingLotId"];
    if(self.plateNumber)
        [jsonObject setObject: self.plateNumber forKey: @"plateNumber"];
    if(self.plateOwnerName)
        [jsonObject setObject: self.plateOwnerName forKey: @"plateOwnerName"];
    if(self.plateOwnerPhone)
        [jsonObject setObject: self.plateOwnerPhone forKey: @"plateOwnerPhone"];
    if(self.payerEnterpriseId)
        [jsonObject setObject: self.payerEnterpriseId forKey: @"payerEnterpriseId"];
    if(self.payerUid)
        [jsonObject setObject: self.payerUid forKey: @"payerUid"];
    if(self.payerName)
        [jsonObject setObject: self.payerName forKey: @"payerName"];
    if(self.payerPhone)
        [jsonObject setObject: self.payerPhone forKey: @"payerPhone"];
    if(self.paidType)
        [jsonObject setObject: self.paidType forKey: @"paidType"];
    if(self.paidTime)
        [jsonObject setObject: self.paidTime forKey: @"paidTime"];
    if(self.vendorName)
        [jsonObject setObject: self.vendorName forKey: @"vendorName"];
    if(self.cardNumber)
        [jsonObject setObject: self.cardNumber forKey: @"cardNumber"];
    if(self.rateToken)
        [jsonObject setObject: self.rateToken forKey: @"rateToken"];
    if(self.rateName)
        [jsonObject setObject: self.rateName forKey: @"rateName"];
    if(self.monthCount)
        [jsonObject setObject: self.monthCount forKey: @"monthCount"];
    if(self.price)
        [jsonObject setObject: self.price forKey: @"price"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.rechargeStatus)
        [jsonObject setObject: self.rechargeStatus forKey: @"rechargeStatus"];
    if(self.rechargeTime)
        [jsonObject setObject: self.rechargeTime forKey: @"rechargeTime"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.parkingLotId = [jsonObject objectForKey: @"parkingLotId"];
        if(self.parkingLotId && [self.parkingLotId isEqual:[NSNull null]])
            self.parkingLotId = nil;

        self.plateNumber = [jsonObject objectForKey: @"plateNumber"];
        if(self.plateNumber && [self.plateNumber isEqual:[NSNull null]])
            self.plateNumber = nil;

        self.plateOwnerName = [jsonObject objectForKey: @"plateOwnerName"];
        if(self.plateOwnerName && [self.plateOwnerName isEqual:[NSNull null]])
            self.plateOwnerName = nil;

        self.plateOwnerPhone = [jsonObject objectForKey: @"plateOwnerPhone"];
        if(self.plateOwnerPhone && [self.plateOwnerPhone isEqual:[NSNull null]])
            self.plateOwnerPhone = nil;

        self.payerEnterpriseId = [jsonObject objectForKey: @"payerEnterpriseId"];
        if(self.payerEnterpriseId && [self.payerEnterpriseId isEqual:[NSNull null]])
            self.payerEnterpriseId = nil;

        self.payerUid = [jsonObject objectForKey: @"payerUid"];
        if(self.payerUid && [self.payerUid isEqual:[NSNull null]])
            self.payerUid = nil;

        self.payerName = [jsonObject objectForKey: @"payerName"];
        if(self.payerName && [self.payerName isEqual:[NSNull null]])
            self.payerName = nil;

        self.payerPhone = [jsonObject objectForKey: @"payerPhone"];
        if(self.payerPhone && [self.payerPhone isEqual:[NSNull null]])
            self.payerPhone = nil;

        self.paidType = [jsonObject objectForKey: @"paidType"];
        if(self.paidType && [self.paidType isEqual:[NSNull null]])
            self.paidType = nil;

        self.paidTime = [jsonObject objectForKey: @"paidTime"];
        if(self.paidTime && [self.paidTime isEqual:[NSNull null]])
            self.paidTime = nil;

        self.vendorName = [jsonObject objectForKey: @"vendorName"];
        if(self.vendorName && [self.vendorName isEqual:[NSNull null]])
            self.vendorName = nil;

        self.cardNumber = [jsonObject objectForKey: @"cardNumber"];
        if(self.cardNumber && [self.cardNumber isEqual:[NSNull null]])
            self.cardNumber = nil;

        self.rateToken = [jsonObject objectForKey: @"rateToken"];
        if(self.rateToken && [self.rateToken isEqual:[NSNull null]])
            self.rateToken = nil;

        self.rateName = [jsonObject objectForKey: @"rateName"];
        if(self.rateName && [self.rateName isEqual:[NSNull null]])
            self.rateName = nil;

        self.monthCount = [jsonObject objectForKey: @"monthCount"];
        if(self.monthCount && [self.monthCount isEqual:[NSNull null]])
            self.monthCount = nil;

        self.price = [jsonObject objectForKey: @"price"];
        if(self.price && [self.price isEqual:[NSNull null]])
            self.price = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.rechargeStatus = [jsonObject objectForKey: @"rechargeStatus"];
        if(self.rechargeStatus && [self.rechargeStatus isEqual:[NSNull null]])
            self.rechargeStatus = nil;

        self.rechargeTime = [jsonObject objectForKey: @"rechargeTime"];
        if(self.rechargeTime && [self.rechargeTime isEqual:[NSNull null]])
            self.rechargeTime = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
