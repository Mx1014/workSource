//
// EvhOpenapiGetUserInfoByIdRestResponse.h
// generated at 2016-04-07 17:57:44 
//
#import "RestResponseBase.h"
#import "EvhUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiGetUserInfoByIdRestResponse
//
@interface EvhOpenapiGetUserInfoByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserInfo* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
