//
// EvhAdminOrgListPersonnelNotJoinGroupsRestResponse.h
// generated at 2016-04-19 12:41:54 
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
