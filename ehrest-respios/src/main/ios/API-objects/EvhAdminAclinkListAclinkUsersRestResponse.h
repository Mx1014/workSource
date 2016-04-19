//
// EvhAdminAclinkListAclinkUsersRestResponse.h
// generated at 2016-04-19 14:25:57 
//
#import "RestResponseBase.h"
#import "EvhAclinkUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkListAclinkUsersRestResponse
//
@interface EvhAdminAclinkListAclinkUsersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhAclinkUserResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
