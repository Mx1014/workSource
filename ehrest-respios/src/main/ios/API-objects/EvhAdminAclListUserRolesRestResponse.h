//
// EvhAdminAclListUserRolesRestResponse.h
// generated at 2016-04-19 12:41:54 
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
