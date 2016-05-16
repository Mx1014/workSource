//
// EvhOAuth2AccessTokenResponse.m
//
#import "EvhOAuth2AccessTokenResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOAuth2AccessTokenResponse
//

@implementation EvhOAuth2AccessTokenResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOAuth2AccessTokenResponse* obj = [EvhOAuth2AccessTokenResponse new];
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
    if(self.access_token)
        [jsonObject setObject: self.access_token forKey: @"access_token"];
    if(self.token_type)
        [jsonObject setObject: self.token_type forKey: @"token_type"];
    if(self.expires_in)
        [jsonObject setObject: self.expires_in forKey: @"expires_in"];
    if(self.refresh_token)
        [jsonObject setObject: self.refresh_token forKey: @"refresh_token"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.access_token = [jsonObject objectForKey: @"access_token"];
        if(self.access_token && [self.access_token isEqual:[NSNull null]])
            self.access_token = nil;

        self.token_type = [jsonObject objectForKey: @"token_type"];
        if(self.token_type && [self.token_type isEqual:[NSNull null]])
            self.token_type = nil;

        self.expires_in = [jsonObject objectForKey: @"expires_in"];
        if(self.expires_in && [self.expires_in isEqual:[NSNull null]])
            self.expires_in = nil;

        self.refresh_token = [jsonObject objectForKey: @"refresh_token"];
        if(self.refresh_token && [self.refresh_token isEqual:[NSNull null]])
            self.refresh_token = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
