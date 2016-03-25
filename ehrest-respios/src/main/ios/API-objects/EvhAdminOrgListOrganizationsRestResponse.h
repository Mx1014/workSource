//
// EvhAdminOrgListOrganizationsRestResponse.h
// generated at 2016-03-25 11:43:34 
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
