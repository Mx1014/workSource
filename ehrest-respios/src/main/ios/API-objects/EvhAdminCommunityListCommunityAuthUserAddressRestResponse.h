//
// EvhAdminCommunityListCommunityAuthUserAddressRestResponse.h
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
