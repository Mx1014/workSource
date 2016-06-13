//
// EvhUiUserGetUserRelatedAddressesRestResponse.h
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
