//
// EvhAdminCommunityListCommunityAuthUserAddressRestResponse.m
//
#import "EvhAdminCommunityListCommunityAuthUserAddressRestResponse.h"
#import "EvhCommunityAuthUserAddressResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityListCommunityAuthUserAddressRestResponse
//

@implementation EvhAdminCommunityListCommunityAuthUserAddressRestResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAdminCommunityListCommunityAuthUserAddressRestResponse* obj = [EvhAdminCommunityListCommunityAuthUserAddressRestResponse new];
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
    [super toJson: jsonObject];
    
    if(self.response) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.response toJson: dic];
        [jsonObject setObject: dic forKey: @"response"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        NSMutableDictionary* dic =  (NSMutableDictionary*)[jsonObject objectForKey: @"response"];
        self.response = [EvhCommunityAuthUserAddressResponse new];
        self.response = [self.response fromJson: dic];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
