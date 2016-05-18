//
// EvhAdminCommunityListUserByNotJoinedCommunityRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCommunityUserAddressResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityListUserByNotJoinedCommunityRestResponse
//
@interface EvhAdminCommunityListUserByNotJoinedCommunityRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityUserAddressResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
