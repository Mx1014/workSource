//
// EvhOpenapiGetUserInfoByUuidRestResponse.h
// generated at 2016-04-19 13:40:01 
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
