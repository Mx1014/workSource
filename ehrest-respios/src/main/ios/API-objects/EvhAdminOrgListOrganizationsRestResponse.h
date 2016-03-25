//
// EvhAdminOrgListOrganizationsRestResponse.h
// generated at 2016-03-25 17:08:12 
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
