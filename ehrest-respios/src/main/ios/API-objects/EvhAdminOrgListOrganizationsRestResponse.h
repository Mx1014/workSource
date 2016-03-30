//
// EvhAdminOrgListOrganizationsRestResponse.h
// generated at 2016-03-30 10:13:09 
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
