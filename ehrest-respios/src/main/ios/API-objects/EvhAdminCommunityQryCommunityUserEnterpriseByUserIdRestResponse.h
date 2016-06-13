//
// EvhAdminCommunityQryCommunityUserEnterpriseByUserIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCommunityUserAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityQryCommunityUserEnterpriseByUserIdRestResponse
//
@interface EvhAdminCommunityQryCommunityUserEnterpriseByUserIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityUserAddressDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
