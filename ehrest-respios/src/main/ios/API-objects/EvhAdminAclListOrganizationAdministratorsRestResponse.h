//
// EvhAdminAclListOrganizationAdministratorsRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhListOrganizationMemberCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclListOrganizationAdministratorsRestResponse
//
@interface EvhAdminAclListOrganizationAdministratorsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrganizationMemberCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
