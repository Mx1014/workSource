//
// EvhOrgListOrganizationCommunitiesV2RestResponse.h
// generated at 2016-04-26 18:22:57 
//
#import "RestResponseBase.h"
#import "EvhListOrganizationCommunityCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListOrganizationCommunitiesV2RestResponse
//
@interface EvhOrgListOrganizationCommunitiesV2RestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrganizationCommunityCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
