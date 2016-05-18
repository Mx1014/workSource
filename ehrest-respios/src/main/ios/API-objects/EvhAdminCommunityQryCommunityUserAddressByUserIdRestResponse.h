//
// EvhAdminCommunityQryCommunityUserAddressByUserIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCommunityUserAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityQryCommunityUserAddressByUserIdRestResponse
//
@interface EvhAdminCommunityQryCommunityUserAddressByUserIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityUserAddressDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
