//
// EvhParkingRechargeRateDTO.m
//
#import "EvhParkingRechargeRateDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingRechargeRateDTO
//

@implementation EvhParkingRechargeRateDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhParkingRechargeRateDTO* obj = [EvhParkingRechargeRateDTO new];
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
    if(self.areaId)
        [jsonObject setObject: self.areaId forKey: @"areaId"];
    if(self.vendorName)
        [jsonObject setObject: self.vendorName forKey: @"vendorName"];
    if(self.rateToken)
        [jsonObject setObject: self.rateToken forKey: @"rateToken"];
    if(self.rateName)
        [jsonObject setObject: self.rateName forKey: @"rateName"];
    if(self.monthCount)
        [jsonObject setObject: self.monthCount forKey: @"monthCount"];
    if(self.price)
        [jsonObject setObject: self.price forKey: @"price"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
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

        self.areaId = [jsonObject objectForKey: @"areaId"];
        if(self.areaId && [self.areaId isEqual:[NSNull null]])
            self.areaId = nil;

        self.vendorName = [jsonObject objectForKey: @"vendorName"];
        if(self.vendorName && [self.vendorName isEqual:[NSNull null]])
            self.vendorName = nil;

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

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
