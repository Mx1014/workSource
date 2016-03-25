//
// EvhOrgListOrganizationCommunitiesRestResponse.h
// generated at 2016-03-25 09:26:44 
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
