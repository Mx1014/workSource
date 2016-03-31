//
// EvhParkingCardDTO.m
// generated at 2016-03-31 10:18:19 
//
#import "EvhParkingCardDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingCardDTO
//

@implementation EvhParkingCardDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhParkingCardDTO* obj = [EvhParkingCardDTO new];
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
    if(self.plateNumber)
        [jsonObject setObject: self.plateNumber forKey: @"plateNumber"];
    if(self.plateOwnerName)
        [jsonObject setObject: self.plateOwnerName forKey: @"plateOwnerName"];
    if(self.plateOwnerPhone)
        [jsonObject setObject: self.plateOwnerPhone forKey: @"plateOwnerPhone"];
    if(self.cardNumber)
        [jsonObject setObject: self.cardNumber forKey: @"cardNumber"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
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

        self.plateNumber = [jsonObject objectForKey: @"plateNumber"];
        if(self.plateNumber && [self.plateNumber isEqual:[NSNull null]])
            self.plateNumber = nil;

        self.plateOwnerName = [jsonObject objectForKey: @"plateOwnerName"];
        if(self.plateOwnerName && [self.plateOwnerName isEqual:[NSNull null]])
            self.plateOwnerName = nil;

        self.plateOwnerPhone = [jsonObject objectForKey: @"plateOwnerPhone"];
        if(self.plateOwnerPhone && [self.plateOwnerPhone isEqual:[NSNull null]])
            self.plateOwnerPhone = nil;

        self.cardNumber = [jsonObject objectForKey: @"cardNumber"];
        if(self.cardNumber && [self.cardNumber isEqual:[NSNull null]])
            self.cardNumber = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
