//
// EvhAdminOrgListOrganizationByNameRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListOrganizationsByNameResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListOrganizationByNameRestResponse
//
@interface EvhAdminOrgListOrganizationByNameRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrganizationsByNameResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
