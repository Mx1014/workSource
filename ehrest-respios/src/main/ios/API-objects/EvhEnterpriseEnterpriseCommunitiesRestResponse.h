//
// EvhEnterpriseEnterpriseCommunitiesRestResponse.h
// generated at 2016-04-07 10:47:33 
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
