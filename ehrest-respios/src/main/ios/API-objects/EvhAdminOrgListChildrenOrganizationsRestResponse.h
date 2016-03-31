//
// EvhAdminOrgListChildrenOrganizationsRestResponse.h
// generated at 2016-03-31 15:43:23 
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
