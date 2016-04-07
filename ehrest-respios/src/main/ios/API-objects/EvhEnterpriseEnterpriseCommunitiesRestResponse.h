//
// EvhEnterpriseEnterpriseCommunitiesRestResponse.h
// generated at 2016-04-07 17:33:50 
//
#import "RestResponseBase.h"
#import "EvhEnterpriseCommunityResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseEnterpriseCommunitiesRestResponse
//
@interface EvhEnterpriseEnterpriseCommunitiesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEnterpriseCommunityResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
