//
// EvhOrganizationDTO2.m
//
#import "EvhOrganizationDTO2.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationDTO2
//

@implementation EvhOrganizationDTO2

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationDTO2* obj = [EvhOrganizationDTO2 new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _communityIds = [NSMutableArray new];
        _tokenList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.orgId)
        [jsonObject setObject: self.orgId forKey: @"orgId"];
    if(self.communityIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.communityIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"communityIds"];
    }
    if(self.tokenList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.tokenList) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"tokenList"];
    }
    if(self.addressId)
        [jsonObject setObject: self.addressId forKey: @"addressId"];
    if(self.orgName)
        [jsonObject setObject: self.orgName forKey: @"orgName"];
    if(self.communityNames)
        [jsonObject setObject: self.communityNames forKey: @"communityNames"];
    if(self.addressName)
        [jsonObject setObject: self.addressName forKey: @"addressName"];
    if(self.orgType)
        [jsonObject setObject: self.orgType forKey: @"orgType"];
    if(self.tokens)
        [jsonObject setObject: self.tokens forKey: @"tokens"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.cityName)
        [jsonObject setObject: self.cityName forKey: @"cityName"];
    if(self.areaId)
        [jsonObject setObject: self.areaId forKey: @"areaId"];
    if(self.areaName)
        [jsonObject setObject: self.areaName forKey: @"areaName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.orgId = [jsonObject objectForKey: @"orgId"];
        if(self.orgId && [self.orgId isEqual:[NSNull null]])
            self.orgId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"communityIds"];
            for(id itemJson in jsonArray) {
                [self.communityIds addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"tokenList"];
            for(id itemJson in jsonArray) {
                [self.tokenList addObject: itemJson];
            }
        }
        self.addressId = [jsonObject objectForKey: @"addressId"];
        if(self.addressId && [self.addressId isEqual:[NSNull null]])
            self.addressId = nil;

        self.orgName = [jsonObject objectForKey: @"orgName"];
        if(self.orgName && [self.orgName isEqual:[NSNull null]])
            self.orgName = nil;

        self.communityNames = [jsonObject objectForKey: @"communityNames"];
        if(self.communityNames && [self.communityNames isEqual:[NSNull null]])
            self.communityNames = nil;

        self.addressName = [jsonObject objectForKey: @"addressName"];
        if(self.addressName && [self.addressName isEqual:[NSNull null]])
            self.addressName = nil;

        self.orgType = [jsonObject objectForKey: @"orgType"];
        if(self.orgType && [self.orgType isEqual:[NSNull null]])
            self.orgType = nil;

        self.tokens = [jsonObject objectForKey: @"tokens"];
        if(self.tokens && [self.tokens isEqual:[NSNull null]])
            self.tokens = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.cityName = [jsonObject objectForKey: @"cityName"];
        if(self.cityName && [self.cityName isEqual:[NSNull null]])
            self.cityName = nil;

        self.areaId = [jsonObject objectForKey: @"areaId"];
        if(self.areaId && [self.areaId isEqual:[NSNull null]])
            self.areaId = nil;

        self.areaName = [jsonObject objectForKey: @"areaName"];
        if(self.areaName && [self.areaName isEqual:[NSNull null]])
            self.areaName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
