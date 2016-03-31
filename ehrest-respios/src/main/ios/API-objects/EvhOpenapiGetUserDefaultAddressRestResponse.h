//
// EvhOpenapiGetUserDefaultAddressRestResponse.h
// generated at 2016-03-31 19:08:54 
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
