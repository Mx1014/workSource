//
// EvhUiUserGetUserRelatedAddressesRestResponse.h
// generated at 2016-04-19 14:25:58 
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
