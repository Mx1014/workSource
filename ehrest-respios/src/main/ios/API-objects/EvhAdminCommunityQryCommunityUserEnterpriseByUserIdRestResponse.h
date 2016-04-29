//
// EvhAdminCommunityQryCommunityUserEnterpriseByUserIdRestResponse.h
// generated at 2016-04-26 18:22:56 
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
