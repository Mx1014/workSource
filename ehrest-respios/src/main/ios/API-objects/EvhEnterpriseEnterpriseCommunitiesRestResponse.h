//
// EvhEnterpriseEnterpriseCommunitiesRestResponse.h
// generated at 2016-03-31 15:43:24 
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
