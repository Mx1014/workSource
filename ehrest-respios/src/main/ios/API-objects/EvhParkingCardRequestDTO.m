//
// EvhParkingCardRequestDTO.m
//
#import "EvhParkingCardRequestDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingCardRequestDTO
//

@implementation EvhParkingCardRequestDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhParkingCardRequestDTO* obj = [EvhParkingCardRequestDTO new];
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
    if(self.requestorEnterpriseId)
        [jsonObject setObject: self.requestorEnterpriseId forKey: @"requestorEnterpriseId"];
    if(self.requestorUid)
        [jsonObject setObject: self.requestorUid forKey: @"requestorUid"];
    if(self.requestorName)
        [jsonObject setObject: self.requestorName forKey: @"requestorName"];
    if(self.plateNumber)
        [jsonObject setObject: self.plateNumber forKey: @"plateNumber"];
    if(self.plateOwnerEntperiseName)
        [jsonObject setObject: self.plateOwnerEntperiseName forKey: @"plateOwnerEntperiseName"];
    if(self.plateOwnerName)
        [jsonObject setObject: self.plateOwnerName forKey: @"plateOwnerName"];
    if(self.plateOwnerPhone)
        [jsonObject setObject: self.plateOwnerPhone forKey: @"plateOwnerPhone"];
    if(self.ranking)
        [jsonObject setObject: self.ranking forKey: @"ranking"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.issueFlag)
        [jsonObject setObject: self.issueFlag forKey: @"issueFlag"];
    if(self.issueTime)
        [jsonObject setObject: self.issueTime forKey: @"issueTime"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
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

        self.requestorEnterpriseId = [jsonObject objectForKey: @"requestorEnterpriseId"];
        if(self.requestorEnterpriseId && [self.requestorEnterpriseId isEqual:[NSNull null]])
            self.requestorEnterpriseId = nil;

        self.requestorUid = [jsonObject objectForKey: @"requestorUid"];
        if(self.requestorUid && [self.requestorUid isEqual:[NSNull null]])
            self.requestorUid = nil;

        self.requestorName = [jsonObject objectForKey: @"requestorName"];
        if(self.requestorName && [self.requestorName isEqual:[NSNull null]])
            self.requestorName = nil;

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

        self.ranking = [jsonObject objectForKey: @"ranking"];
        if(self.ranking && [self.ranking isEqual:[NSNull null]])
            self.ranking = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.issueFlag = [jsonObject objectForKey: @"issueFlag"];
        if(self.issueFlag && [self.issueFlag isEqual:[NSNull null]])
            self.issueFlag = nil;

        self.issueTime = [jsonObject objectForKey: @"issueTime"];
        if(self.issueTime && [self.issueTime isEqual:[NSNull null]])
            self.issueTime = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
