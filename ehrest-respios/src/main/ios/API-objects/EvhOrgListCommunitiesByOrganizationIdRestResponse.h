//
// EvhOrgListCommunitiesByOrganizationIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListCommunityByNamespaceCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListCommunitiesByOrganizationIdRestResponse
//
@interface EvhOrgListCommunitiesByOrganizationIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListCommunityByNamespaceCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
