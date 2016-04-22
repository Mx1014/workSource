//
// EvhOpenapiGetUserInfoByUuidRestResponse.h
// generated at 2016-04-22 13:56:50 
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
