//
// EvhAdminAclListWebMenuPrivilegeRestResponse.h
// generated at 2016-04-08 20:09:23 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclListWebMenuPrivilegeRestResponse
//
@interface EvhAdminAclListWebMenuPrivilegeRestResponse : EvhRestResponseBase

// array of EvhListWebMenuPrivilegeDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
