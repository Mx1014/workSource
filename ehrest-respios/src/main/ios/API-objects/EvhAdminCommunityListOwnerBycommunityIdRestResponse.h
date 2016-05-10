//
// EvhAdminCommunityListOwnerBycommunityIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCommunityUserAddressResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityListOwnerBycommunityIdRestResponse
//
@interface EvhAdminCommunityListOwnerBycommunityIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityUserAddressResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
