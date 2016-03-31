//
// EvhAdminAclinkListAclinkUsersRestResponse.h
// generated at 2016-03-31 19:08:54 
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
