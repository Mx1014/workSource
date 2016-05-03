//
// EvhAdminCommunityListCommunityAuthUserAddressRestResponse.h
// generated at 2016-04-29 18:56:03 
//
#import "RestResponseBase.h"
#import "EvhCommunityAuthUserAddressResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityListCommunityAuthUserAddressRestResponse
//
@interface EvhAdminCommunityListCommunityAuthUserAddressRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityAuthUserAddressResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
