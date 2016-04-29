//
// EvhAdminCommunityListOwnerBycommunityIdRestResponse.h
// generated at 2016-04-26 18:22:56 
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
