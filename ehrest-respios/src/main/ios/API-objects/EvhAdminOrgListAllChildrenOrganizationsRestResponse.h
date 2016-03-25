//
// EvhAdminOrgListAllChildrenOrganizationsRestResponse.h
// generated at 2016-03-25 17:08:12 
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
