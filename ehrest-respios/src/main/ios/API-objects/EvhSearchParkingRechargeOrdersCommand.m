//
// EvhSearchParkingRechargeOrdersCommand.m
// generated at 2016-03-31 10:18:20 
//
#import "EvhSearchParkingRechargeOrdersCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchParkingRechargeOrdersCommand
//

@implementation EvhSearchParkingRechargeOrdersCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSearchParkingRechargeOrdersCommand* obj = [EvhSearchParkingRechargeOrdersCommand new];
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
    if(self.payerName)
        [jsonObject setObject: self.payerName forKey: @"payerName"];
    if(self.payerPhone)
        [jsonObject setObject: self.payerPhone forKey: @"payerPhone"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
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

        self.payerName = [jsonObject objectForKey: @"payerName"];
        if(self.payerName && [self.payerName isEqual:[NSNull null]])
            self.payerName = nil;

        self.payerPhone = [jsonObject objectForKey: @"payerPhone"];
        if(self.payerPhone && [self.payerPhone isEqual:[NSNull null]])
            self.payerPhone = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
