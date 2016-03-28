//
// EvhOpenapiGetUserDefaultAddressRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"
#import "EvhUserServiceAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiGetUserDefaultAddressRestResponse
//
@interface EvhOpenapiGetUserDefaultAddressRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserServiceAddressDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
