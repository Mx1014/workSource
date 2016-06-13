//
// EvhGetUserDetailByUuidResponse.m
//
#import "EvhGetUserDetailByUuidResponse.h"
#import "EvhUserServiceAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserDetailByUuidResponse
//

@implementation EvhGetUserDetailByUuidResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetUserDetailByUuidResponse* obj = [EvhGetUserDetailByUuidResponse new];
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
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.avatarUrl)
        [jsonObject setObject: self.avatarUrl forKey: @"avatarUrl"];
    if(self.gender)
        [jsonObject setObject: self.gender forKey: @"gender"];
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
    if(self.address) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.address toJson: dic];
        
        [jsonObject setObject: dic forKey: @"address"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.avatarUrl = [jsonObject objectForKey: @"avatarUrl"];
        if(self.avatarUrl && [self.avatarUrl isEqual:[NSNull null]])
            self.avatarUrl = nil;

        self.gender = [jsonObject objectForKey: @"gender"];
        if(self.gender && [self.gender isEqual:[NSNull null]])
            self.gender = nil;

        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"address"];

        self.address = [EvhUserServiceAddressDTO new];
        self.address = [self.address fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
