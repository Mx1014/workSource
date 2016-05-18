//
// EvhAdminOrgListPersonnelNotJoinGroupsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListOrganizationMemberCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListPersonnelNotJoinGroupsRestResponse
//
@interface EvhAdminOrgListPersonnelNotJoinGroupsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrganizationMemberCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
