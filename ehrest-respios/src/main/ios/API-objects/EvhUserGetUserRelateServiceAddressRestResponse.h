//
// EvhUserGetUserRelateServiceAddressRestResponse.h
// generated at 2016-03-28 15:56:09 
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
