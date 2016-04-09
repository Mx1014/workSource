//
// EvhOAuth2AccessTokenResponse.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOAuth2AccessTokenResponse
//
@interface EvhOAuth2AccessTokenResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* access_token;

@property(nonatomic, copy) NSString* token_type;

@property(nonatomic, copy) NSNumber* expires_in;

@property(nonatomic, copy) NSString* refresh_token;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

