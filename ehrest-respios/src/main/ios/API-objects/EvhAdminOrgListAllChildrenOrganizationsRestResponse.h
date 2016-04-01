//
// EvhAdminOrgListAllChildrenOrganizationsRestResponse.h
// generated at 2016-03-31 20:15:33 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListAllChildrenOrganizationsRestResponse
//
@interface EvhAdminOrgListAllChildrenOrganizationsRestResponse : EvhRestResponseBase

// array of EvhOrganizationDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
