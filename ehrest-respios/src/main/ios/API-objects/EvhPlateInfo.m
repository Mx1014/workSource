//
// EvhPlateInfo.m
//
#import "EvhPlateInfo.h"
#import "EvhParkingChargeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPlateInfo
//

@implementation EvhPlateInfo

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPlateInfo* obj = [EvhPlateInfo new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _parkingCharge = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.plateNumber)
        [jsonObject setObject: self.plateNumber forKey: @"plateNumber"];
    if(self.ownerName)
        [jsonObject setObject: self.ownerName forKey: @"ownerName"];
    if(self.validityPeriod)
        [jsonObject setObject: self.validityPeriod forKey: @"validityPeriod"];
    if(self.cardType)
        [jsonObject setObject: self.cardType forKey: @"cardType"];
    if(self.cardCode)
        [jsonObject setObject: self.cardCode forKey: @"cardCode"];
    if(self.isValid)
        [jsonObject setObject: self.isValid forKey: @"isValid"];
    if(self.parkingCharge) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhParkingChargeDTO* item in self.parkingCharge) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"parkingCharge"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.plateNumber = [jsonObject objectForKey: @"plateNumber"];
        if(self.plateNumber && [self.plateNumber isEqual:[NSNull null]])
            self.plateNumber = nil;

        self.ownerName = [jsonObject objectForKey: @"ownerName"];
        if(self.ownerName && [self.ownerName isEqual:[NSNull null]])
            self.ownerName = nil;

        self.validityPeriod = [jsonObject objectForKey: @"validityPeriod"];
        if(self.validityPeriod && [self.validityPeriod isEqual:[NSNull null]])
            self.validityPeriod = nil;

        self.cardType = [jsonObject objectForKey: @"cardType"];
        if(self.cardType && [self.cardType isEqual:[NSNull null]])
            self.cardType = nil;

        self.cardCode = [jsonObject objectForKey: @"cardCode"];
        if(self.cardCode && [self.cardCode isEqual:[NSNull null]])
            self.cardCode = nil;

        self.isValid = [jsonObject objectForKey: @"isValid"];
        if(self.isValid && [self.isValid isEqual:[NSNull null]])
            self.isValid = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"parkingCharge"];
            for(id itemJson in jsonArray) {
                EvhParkingChargeDTO* item = [EvhParkingChargeDTO new];
                
                [item fromJson: itemJson];
                [self.parkingCharge addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
