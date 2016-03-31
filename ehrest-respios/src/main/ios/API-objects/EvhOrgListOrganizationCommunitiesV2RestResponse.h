//
// EvhOrgListOrganizationCommunitiesV2RestResponse.h
// generated at 2016-03-28 15:56:09 
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
