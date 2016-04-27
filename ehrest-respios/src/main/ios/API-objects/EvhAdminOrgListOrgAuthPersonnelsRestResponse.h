//
// EvhAdminOrgListOrgAuthPersonnelsRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhListOrganizationMemberCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListOrgAuthPersonnelsRestResponse
//
@interface EvhAdminOrgListOrgAuthPersonnelsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrganizationMemberCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
