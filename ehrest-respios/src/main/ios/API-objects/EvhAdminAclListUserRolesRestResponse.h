//
// EvhAdminAclListUserRolesRestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"
#import "EvhListUserRolesAdminCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclListUserRolesRestResponse
//
@interface EvhAdminAclListUserRolesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListUserRolesAdminCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
