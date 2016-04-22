//
// EvhAdminOrgListAllOrganizationPersonnelsRestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"
#import "EvhListOrganizationMemberCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListAllOrganizationPersonnelsRestResponse
//
@interface EvhAdminOrgListAllOrganizationPersonnelsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrganizationMemberCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
