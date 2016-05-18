//
// EvhAdminCommunityListUserBycommunityIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCommunityUserAddressResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityListUserBycommunityIdRestResponse
//
@interface EvhAdminCommunityListUserBycommunityIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityUserAddressResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
