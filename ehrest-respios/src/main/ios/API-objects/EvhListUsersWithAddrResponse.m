//
// EvhListUsersWithAddrResponse.m
//
#import "EvhListUsersWithAddrResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUsersWithAddrResponse
//

@implementation EvhListUsersWithAddrResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListUsersWithAddrResponse* obj = [EvhListUsersWithAddrResponse new];
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
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.cellPhone)
        [jsonObject setObject: self.cellPhone forKey: @"cellPhone"];
    if(self.cityName)
        [jsonObject setObject: self.cityName forKey: @"cityName"];
    if(self.areaName)
        [jsonObject setObject: self.areaName forKey: @"areaName"];
    if(self.communityName)
        [jsonObject setObject: self.communityName forKey: @"communityName"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.apartmentName)
        [jsonObject setObject: self.apartmentName forKey: @"apartmentName"];
    if(self.addressStatus)
        [jsonObject setObject: self.addressStatus forKey: @"addressStatus"];
    if(self.apartmentStatus)
        [jsonObject setObject: self.apartmentStatus forKey: @"apartmentStatus"];
    if(self.familyName)
        [jsonObject setObject: self.familyName forKey: @"familyName"];
    if(self.cellPhoneNumberLocation)
        [jsonObject setObject: self.cellPhoneNumberLocation forKey: @"cellPhoneNumberLocation"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.cellPhone = [jsonObject objectForKey: @"cellPhone"];
        if(self.cellPhone && [self.cellPhone isEqual:[NSNull null]])
            self.cellPhone = nil;

        self.cityName = [jsonObject objectForKey: @"cityName"];
        if(self.cityName && [self.cityName isEqual:[NSNull null]])
            self.cityName = nil;

        self.areaName = [jsonObject objectForKey: @"areaName"];
        if(self.areaName && [self.areaName isEqual:[NSNull null]])
            self.areaName = nil;

        self.communityName = [jsonObject objectForKey: @"communityName"];
        if(self.communityName && [self.communityName isEqual:[NSNull null]])
            self.communityName = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.apartmentName = [jsonObject objectForKey: @"apartmentName"];
        if(self.apartmentName && [self.apartmentName isEqual:[NSNull null]])
            self.apartmentName = nil;

        self.addressStatus = [jsonObject objectForKey: @"addressStatus"];
        if(self.addressStatus && [self.addressStatus isEqual:[NSNull null]])
            self.addressStatus = nil;

        self.apartmentStatus = [jsonObject objectForKey: @"apartmentStatus"];
        if(self.apartmentStatus && [self.apartmentStatus isEqual:[NSNull null]])
            self.apartmentStatus = nil;

        self.familyName = [jsonObject objectForKey: @"familyName"];
        if(self.familyName && [self.familyName isEqual:[NSNull null]])
            self.familyName = nil;

        self.cellPhoneNumberLocation = [jsonObject objectForKey: @"cellPhoneNumberLocation"];
        if(self.cellPhoneNumberLocation && [self.cellPhoneNumberLocation isEqual:[NSNull null]])
            self.cellPhoneNumberLocation = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
