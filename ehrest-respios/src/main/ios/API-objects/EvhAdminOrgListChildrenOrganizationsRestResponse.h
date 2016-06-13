//
// EvhAdminOrgListChildrenOrganizationsRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListChildrenOrganizationsRestResponse
//
@interface EvhAdminOrgListChildrenOrganizationsRestResponse : EvhRestResponseBase

// array of EvhOrganizationDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
