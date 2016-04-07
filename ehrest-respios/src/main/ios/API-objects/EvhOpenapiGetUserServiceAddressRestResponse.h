//
// EvhOpenapiGetUserServiceAddressRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiGetUserServiceAddressRestResponse
//
@interface EvhOpenapiGetUserServiceAddressRestResponse : EvhRestResponseBase

// array of EvhUserServiceAddressDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
