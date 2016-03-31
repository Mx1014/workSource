//
// EvhOpenapiGetUserInfoByUuidRestResponse.h
// generated at 2016-03-31 19:08:54 
//
#import "RestResponseBase.h"
#import "EvhGetUserByUuidResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiGetUserInfoByUuidRestResponse
//
@interface EvhOpenapiGetUserInfoByUuidRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetUserByUuidResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
