//
// EvhAdminOrgListAclRoleByUserIdRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgListAclRoleByUserIdRestResponse
//
@interface EvhAdminOrgListAclRoleByUserIdRestResponse : EvhRestResponseBase

// array of EvhOrganizationDetailDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
