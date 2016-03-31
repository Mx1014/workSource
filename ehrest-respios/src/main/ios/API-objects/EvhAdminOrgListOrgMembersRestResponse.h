//
// EvhAdminOrgListOrgMembersRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"
#import "EvhListOrganizationMemberCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListOrgMembersRestResponse
//
@interface EvhAdminOrgListOrgMembersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrganizationMemberCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
