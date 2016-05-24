//
// EvhCommunityUserAddressDTO.m
//
#import "EvhCommunityUserAddressDTO.h"
#import "EvhOrganizationDetailDTO.h"
#import "EvhAddressAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityUserAddressDTO
//

@implementation EvhCommunityUserAddressDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommunityUserAddressDTO* obj = [EvhCommunityUserAddressDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _orgDtos = [NSMutableArray new];
        _addressDtos = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.nikeName)
        [jsonObject setObject: self.nikeName forKey: @"nikeName"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.isAuth)
        [jsonObject setObject: self.isAuth forKey: @"isAuth"];
    if(self.applyTime)
        [jsonObject setObject: self.applyTime forKey: @"applyTime"];
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
    if(self.gender)
        [jsonObject setObject: self.gender forKey: @"gender"];
    if(self.orgDtos) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhOrganizationDetailDTO* item in self.orgDtos) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"orgDtos"];
    }
    if(self.addressDtos) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhAddressAddressDTO* item in self.addressDtos) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"addressDtos"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.nikeName = [jsonObject objectForKey: @"nikeName"];
        if(self.nikeName && [self.nikeName isEqual:[NSNull null]])
            self.nikeName = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.isAuth = [jsonObject objectForKey: @"isAuth"];
        if(self.isAuth && [self.isAuth isEqual:[NSNull null]])
            self.isAuth = nil;

        self.applyTime = [jsonObject objectForKey: @"applyTime"];
        if(self.applyTime && [self.applyTime isEqual:[NSNull null]])
            self.applyTime = nil;

        self.phone = [jsonObject objectForKey: @"phone"];
        if(self.phone && [self.phone isEqual:[NSNull null]])
            self.phone = nil;

        self.gender = [jsonObject objectForKey: @"gender"];
        if(self.gender && [self.gender isEqual:[NSNull null]])
            self.gender = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"orgDtos"];
            for(id itemJson in jsonArray) {
                EvhOrganizationDetailDTO* item = [EvhOrganizationDetailDTO new];
                
                [item fromJson: itemJson];
                [self.orgDtos addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"addressDtos"];
            for(id itemJson in jsonArray) {
                EvhAddressAddressDTO* item = [EvhAddressAddressDTO new];
                
                [item fromJson: itemJson];
                [self.addressDtos addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
