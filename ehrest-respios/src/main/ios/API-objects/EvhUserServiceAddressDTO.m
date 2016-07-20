//
// EvhUserServiceAddressDTO.m
//
#import "EvhUserServiceAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserServiceAddressDTO
//

@implementation EvhUserServiceAddressDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserServiceAddressDTO* obj = [EvhUserServiceAddressDTO new];
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
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.province)
        [jsonObject setObject: self.province forKey: @"province"];
    if(self.city)
        [jsonObject setObject: self.city forKey: @"city"];
    if(self.area)
        [jsonObject setObject: self.area forKey: @"area"];
    if(self.callPhone)
        [jsonObject setObject: self.callPhone forKey: @"callPhone"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.communityName)
        [jsonObject setObject: self.communityName forKey: @"communityName"];
    if(self.addressType)
        [jsonObject setObject: self.addressType forKey: @"addressType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.province = [jsonObject objectForKey: @"province"];
        if(self.province && [self.province isEqual:[NSNull null]])
            self.province = nil;

        self.city = [jsonObject objectForKey: @"city"];
        if(self.city && [self.city isEqual:[NSNull null]])
            self.city = nil;

        self.area = [jsonObject objectForKey: @"area"];
        if(self.area && [self.area isEqual:[NSNull null]])
            self.area = nil;

        self.callPhone = [jsonObject objectForKey: @"callPhone"];
        if(self.callPhone && [self.callPhone isEqual:[NSNull null]])
            self.callPhone = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.communityName = [jsonObject objectForKey: @"communityName"];
        if(self.communityName && [self.communityName isEqual:[NSNull null]])
            self.communityName = nil;

        self.addressType = [jsonObject objectForKey: @"addressType"];
        if(self.addressType && [self.addressType isEqual:[NSNull null]])
            self.addressType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
