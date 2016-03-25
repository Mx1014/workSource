//
// EvhOpenapiGetUserInfoByIdRestResponse.h
// generated at 2016-03-25 11:43:35 
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
