//
// EvhOpenapiGetUserServiceAddressRestResponse.h
// generated at 2016-04-07 17:57:44 
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
