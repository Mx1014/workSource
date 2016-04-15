//
// EvhAdminOrgListOrganizationsRestResponse.h
// generated at 2016-04-12 15:02:21 
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
