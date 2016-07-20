//
// EvhRequestParkingCardCommand.m
//
#import "EvhRequestParkingCardCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRequestParkingCardCommand
//

@implementation EvhRequestParkingCardCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRequestParkingCardCommand* obj = [EvhRequestParkingCardCommand new];
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
    if(self.requestorEnterpriseId)
        [jsonObject setObject: self.requestorEnterpriseId forKey: @"requestorEnterpriseId"];
    if(self.plateNumber)
        [jsonObject setObject: self.plateNumber forKey: @"plateNumber"];
    if(self.plateOwnerEntperiseName)
        [jsonObject setObject: self.plateOwnerEntperiseName forKey: @"plateOwnerEntperiseName"];
    if(self.plateOwnerName)
        [jsonObject setObject: self.plateOwnerName forKey: @"plateOwnerName"];
    if(self.plateOwnerPhone)
        [jsonObject setObject: self.plateOwnerPhone forKey: @"plateOwnerPhone"];
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

        self.requestorEnterpriseId = [jsonObject objectForKey: @"requestorEnterpriseId"];
        if(self.requestorEnterpriseId && [self.requestorEnterpriseId isEqual:[NSNull null]])
            self.requestorEnterpriseId = nil;

        self.plateNumber = [jsonObject objectForKey: @"plateNumber"];
        if(self.plateNumber && [self.plateNumber isEqual:[NSNull null]])
            self.plateNumber = nil;

        self.plateOwnerEntperiseName = [jsonObject objectForKey: @"plateOwnerEntperiseName"];
        if(self.plateOwnerEntperiseName && [self.plateOwnerEntperiseName isEqual:[NSNull null]])
            self.plateOwnerEntperiseName = nil;

        self.plateOwnerName = [jsonObject objectForKey: @"plateOwnerName"];
        if(self.plateOwnerName && [self.plateOwnerName isEqual:[NSNull null]])
            self.plateOwnerName = nil;

        self.plateOwnerPhone = [jsonObject objectForKey: @"plateOwnerPhone"];
        if(self.plateOwnerPhone && [self.plateOwnerPhone isEqual:[NSNull null]])
            self.plateOwnerPhone = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
