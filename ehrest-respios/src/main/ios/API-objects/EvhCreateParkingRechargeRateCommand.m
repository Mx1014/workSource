//
// EvhCreateParkingRechargeRateCommand.m
//
#import "EvhCreateParkingRechargeRateCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateParkingRechargeRateCommand
//

@implementation EvhCreateParkingRechargeRateCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateParkingRechargeRateCommand* obj = [EvhCreateParkingRechargeRateCommand new];
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
    if(self.rateName)
        [jsonObject setObject: self.rateName forKey: @"rateName"];
    if(self.monthCount)
        [jsonObject setObject: self.monthCount forKey: @"monthCount"];
    if(self.price)
        [jsonObject setObject: self.price forKey: @"price"];
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

        self.rateName = [jsonObject objectForKey: @"rateName"];
        if(self.rateName && [self.rateName isEqual:[NSNull null]])
            self.rateName = nil;

        self.monthCount = [jsonObject objectForKey: @"monthCount"];
        if(self.monthCount && [self.monthCount isEqual:[NSNull null]])
            self.monthCount = nil;

        self.price = [jsonObject objectForKey: @"price"];
        if(self.price && [self.price isEqual:[NSNull null]])
            self.price = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
