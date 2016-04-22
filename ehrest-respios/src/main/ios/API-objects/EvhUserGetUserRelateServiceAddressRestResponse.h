//
// EvhUserGetUserRelateServiceAddressRestResponse.h
// generated at 2016-04-22 13:56:52 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserGetUserRelateServiceAddressRestResponse
//
@interface EvhUserGetUserRelateServiceAddressRestResponse : EvhRestResponseBase

// array of EvhUserServiceAddressDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
