//
// EvhDeleteParkingRechargeRateCommand.m
//
#import "EvhDeleteParkingRechargeRateCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteParkingRechargeRateCommand
//

@implementation EvhDeleteParkingRechargeRateCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteParkingRechargeRateCommand* obj = [EvhDeleteParkingRechargeRateCommand new];
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
    if(self.vendorName)
        [jsonObject setObject: self.vendorName forKey: @"vendorName"];
    if(self.rateToken)
        [jsonObject setObject: self.rateToken forKey: @"rateToken"];
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

        self.vendorName = [jsonObject objectForKey: @"vendorName"];
        if(self.vendorName && [self.vendorName isEqual:[NSNull null]])
            self.vendorName = nil;

        self.rateToken = [jsonObject objectForKey: @"rateToken"];
        if(self.rateToken && [self.rateToken isEqual:[NSNull null]])
            self.rateToken = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
