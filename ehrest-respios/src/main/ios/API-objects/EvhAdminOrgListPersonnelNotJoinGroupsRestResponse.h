//
// EvhAdminOrgListPersonnelNotJoinGroupsRestResponse.h
// generated at 2016-04-12 15:02:21 
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
