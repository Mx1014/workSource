//
// EvhOpenapiGetUserDetailByUuidRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhGetUserDetailByUuidResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiGetUserDetailByUuidRestResponse
//
@interface EvhOpenapiGetUserDetailByUuidRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetUserDetailByUuidResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
