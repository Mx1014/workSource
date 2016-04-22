//
// EvhAdminCommunityListOwnerBycommunityIdRestResponse.h
// generated at 2016-04-22 13:56:49 
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
