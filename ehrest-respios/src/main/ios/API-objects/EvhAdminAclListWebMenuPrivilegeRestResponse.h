//
// EvhAdminAclListWebMenuPrivilegeRestResponse.h
// generated at 2016-03-25 19:05:21 
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
