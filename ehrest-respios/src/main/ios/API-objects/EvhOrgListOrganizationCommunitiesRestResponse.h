//
// EvhOrgListOrganizationCommunitiesRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhListOrganizationCommunityCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListOrganizationCommunitiesRestResponse
//
@interface EvhOrgListOrganizationCommunitiesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrganizationCommunityCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
