//
// EvhOpenapiGetUserDetailByUuidRestResponse.h
// generated at 2016-04-12 15:02:21 
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
