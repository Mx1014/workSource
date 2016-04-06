//
// EvhAdminAclListAclRoleByOrganizationIdRestResponse.h
// generated at 2016-04-06 19:10:43 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclListAclRoleByOrganizationIdRestResponse
//
@interface EvhAdminAclListAclRoleByOrganizationIdRestResponse : EvhRestResponseBase

// array of EvhRoleDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
