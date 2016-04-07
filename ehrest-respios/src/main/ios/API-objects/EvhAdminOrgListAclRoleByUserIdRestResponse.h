//
// EvhAdminOrgListAclRoleByUserIdRestResponse.h
// generated at 2016-04-07 10:47:32 
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
