//
// EvhAddressAddressDTO.m
//
#import "EvhAddressAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressAddressDTO
//

@implementation EvhAddressAddressDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddressAddressDTO* obj = [EvhAddressAddressDTO new];
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
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.zipcode)
        [jsonObject setObject: self.zipcode forKey: @"zipcode"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.geohash)
        [jsonObject setObject: self.geohash forKey: @"geohash"];
    if(self.addressAlias)
        [jsonObject setObject: self.addressAlias forKey: @"addressAlias"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.buildingAliasName)
        [jsonObject setObject: self.buildingAliasName forKey: @"buildingAliasName"];
    if(self.apartmentName)
        [jsonObject setObject: self.apartmentName forKey: @"apartmentName"];
    if(self.apartmentFloor)
        [jsonObject setObject: self.apartmentFloor forKey: @"apartmentFloor"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.deleteTime)
        [jsonObject setObject: self.deleteTime forKey: @"deleteTime"];
    if(self.memberStatus)
        [jsonObject setObject: self.memberStatus forKey: @"memberStatus"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.zipcode = [jsonObject objectForKey: @"zipcode"];
        if(self.zipcode && [self.zipcode isEqual:[NSNull null]])
            self.zipcode = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.geohash = [jsonObject objectForKey: @"geohash"];
        if(self.geohash && [self.geohash isEqual:[NSNull null]])
            self.geohash = nil;

        self.addressAlias = [jsonObject objectForKey: @"addressAlias"];
        if(self.addressAlias && [self.addressAlias isEqual:[NSNull null]])
            self.addressAlias = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.buildingAliasName = [jsonObject objectForKey: @"buildingAliasName"];
        if(self.buildingAliasName && [self.buildingAliasName isEqual:[NSNull null]])
            self.buildingAliasName = nil;

        self.apartmentName = [jsonObject objectForKey: @"apartmentName"];
        if(self.apartmentName && [self.apartmentName isEqual:[NSNull null]])
            self.apartmentName = nil;

        self.apartmentFloor = [jsonObject objectForKey: @"apartmentFloor"];
        if(self.apartmentFloor && [self.apartmentFloor isEqual:[NSNull null]])
            self.apartmentFloor = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.deleteTime = [jsonObject objectForKey: @"deleteTime"];
        if(self.deleteTime && [self.deleteTime isEqual:[NSNull null]])
            self.deleteTime = nil;

        self.memberStatus = [jsonObject objectForKey: @"memberStatus"];
        if(self.memberStatus && [self.memberStatus isEqual:[NSNull null]])
            self.memberStatus = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
