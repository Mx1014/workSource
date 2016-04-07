//
// EvhOpenapiGetUserDefaultAddressRestResponse.h
// generated at 2016-04-07 17:03:18 
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
