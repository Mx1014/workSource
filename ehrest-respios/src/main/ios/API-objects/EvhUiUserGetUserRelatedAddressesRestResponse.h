//
// EvhUiUserGetUserRelatedAddressesRestResponse.h
// generated at 2016-03-25 11:43:35 
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
