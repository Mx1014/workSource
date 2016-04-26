//
// EvhAdminOrgListOrganizationsRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhListOrganizationsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListOrganizationsRestResponse
//
@interface EvhAdminOrgListOrganizationsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrganizationsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
