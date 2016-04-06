//
// EvhAdminAclListAclRolesRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclListAclRolesRestResponse
//
@interface EvhAdminAclListAclRolesRestResponse : EvhRestResponseBase

// array of EvhRoleDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
