//
// EvhCreateParkingRechargeOrderCommand.m
//
#import "EvhCreateParkingRechargeOrderCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateParkingRechargeOrderCommand
//

@implementation EvhCreateParkingRechargeOrderCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateParkingRechargeOrderCommand* obj = [EvhCreateParkingRechargeOrderCommand new];
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
    if(self.expiredTime)
        [jsonObject setObject: self.expiredTime forKey: @"expiredTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
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

        self.expiredTime = [jsonObject objectForKey: @"expiredTime"];
        if(self.expiredTime && [self.expiredTime isEqual:[NSNull null]])
            self.expiredTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
