//
// EvhAdminAclListUserRolesRestResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
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
