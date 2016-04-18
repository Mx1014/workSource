//
// EvhAdminAclListAclRoleByOrganizationIdRestResponse.h
// generated at 2016-04-18 14:48:52 
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
