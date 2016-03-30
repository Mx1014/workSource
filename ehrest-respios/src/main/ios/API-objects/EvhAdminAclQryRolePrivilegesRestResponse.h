//
// EvhAdminAclQryRolePrivilegesRestResponse.h
// generated at 2016-03-30 10:13:09 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclQryRolePrivilegesRestResponse
//
@interface EvhAdminAclQryRolePrivilegesRestResponse : EvhRestResponseBase

// array of EvhListWebMenuPrivilegeDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
