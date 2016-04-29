//
// EvhOAuth2AccessTokenResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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

