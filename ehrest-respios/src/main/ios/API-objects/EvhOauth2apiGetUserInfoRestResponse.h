//
// EvhOauth2apiGetUserInfoRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOauth2apiGetUserInfoRestResponse
//
@interface EvhOauth2apiGetUserInfoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserInfo* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
