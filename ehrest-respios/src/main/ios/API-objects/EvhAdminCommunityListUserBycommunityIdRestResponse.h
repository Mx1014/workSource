//
// EvhAdminCommunityListUserBycommunityIdRestResponse.h
// generated at 2016-04-29 18:56:03 
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
