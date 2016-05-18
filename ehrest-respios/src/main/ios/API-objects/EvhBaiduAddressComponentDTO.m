//
// EvhBaiduAddressComponentDTO.m
//
#import "EvhBaiduAddressComponentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBaiduAddressComponentDTO
//

@implementation EvhBaiduAddressComponentDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBaiduAddressComponentDTO* obj = [EvhBaiduAddressComponentDTO new];
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
    if(self.country)
        [jsonObject setObject: self.country forKey: @"country"];
    if(self.province)
        [jsonObject setObject: self.province forKey: @"province"];
    if(self.city)
        [jsonObject setObject: self.city forKey: @"city"];
    if(self.district)
        [jsonObject setObject: self.district forKey: @"district"];
    if(self.street)
        [jsonObject setObject: self.street forKey: @"street"];
    if(self.street_number)
        [jsonObject setObject: self.street_number forKey: @"street_number"];
    if(self.adcode)
        [jsonObject setObject: self.adcode forKey: @"adcode"];
    if(self.country_code)
        [jsonObject setObject: self.country_code forKey: @"country_code"];
    if(self.direction)
        [jsonObject setObject: self.direction forKey: @"direction"];
    if(self.distance)
        [jsonObject setObject: self.distance forKey: @"distance"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.country = [jsonObject objectForKey: @"country"];
        if(self.country && [self.country isEqual:[NSNull null]])
            self.country = nil;

        self.province = [jsonObject objectForKey: @"province"];
        if(self.province && [self.province isEqual:[NSNull null]])
            self.province = nil;

        self.city = [jsonObject objectForKey: @"city"];
        if(self.city && [self.city isEqual:[NSNull null]])
            self.city = nil;

        self.district = [jsonObject objectForKey: @"district"];
        if(self.district && [self.district isEqual:[NSNull null]])
            self.district = nil;

        self.street = [jsonObject objectForKey: @"street"];
        if(self.street && [self.street isEqual:[NSNull null]])
            self.street = nil;

        self.street_number = [jsonObject objectForKey: @"street_number"];
        if(self.street_number && [self.street_number isEqual:[NSNull null]])
            self.street_number = nil;

        self.adcode = [jsonObject objectForKey: @"adcode"];
        if(self.adcode && [self.adcode isEqual:[NSNull null]])
            self.adcode = nil;

        self.country_code = [jsonObject objectForKey: @"country_code"];
        if(self.country_code && [self.country_code isEqual:[NSNull null]])
            self.country_code = nil;

        self.direction = [jsonObject objectForKey: @"direction"];
        if(self.direction && [self.direction isEqual:[NSNull null]])
            self.direction = nil;

        self.distance = [jsonObject objectForKey: @"distance"];
        if(self.distance && [self.distance isEqual:[NSNull null]])
            self.distance = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
