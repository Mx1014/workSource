//
// EvhParkingCardDTO.m
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
    if(self.cardType)
        [jsonObject setObject: self.cardType forKey: @"cardType"];
    if(self.cardNumber)
        [jsonObject setObject: self.cardNumber forKey: @"cardNumber"];
    if(self.cardName)
        [jsonObject setObject: self.cardName forKey: @"cardName"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.isValid)
        [jsonObject setObject: self.isValid forKey: @"isValid"];
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

        self.cardType = [jsonObject objectForKey: @"cardType"];
        if(self.cardType && [self.cardType isEqual:[NSNull null]])
            self.cardType = nil;

        self.cardNumber = [jsonObject objectForKey: @"cardNumber"];
        if(self.cardNumber && [self.cardNumber isEqual:[NSNull null]])
            self.cardNumber = nil;

        self.cardName = [jsonObject objectForKey: @"cardName"];
        if(self.cardName && [self.cardName isEqual:[NSNull null]])
            self.cardName = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.isValid = [jsonObject objectForKey: @"isValid"];
        if(self.isValid && [self.isValid isEqual:[NSNull null]])
            self.isValid = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
