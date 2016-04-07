//
// EvhUiUserGetUserRelatedAddressesRestResponse.h
// generated at 2016-04-07 10:47:33 
//
#import "RestResponseBase.h"
#import "EvhGetUserRelatedAddressResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiUserGetUserRelatedAddressesRestResponse
//
@interface EvhUiUserGetUserRelatedAddressesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetUserRelatedAddressResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
