//
// EvhAdminAclListUserRolesRestResponse.h
// generated at 2016-03-25 19:05:21 
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
