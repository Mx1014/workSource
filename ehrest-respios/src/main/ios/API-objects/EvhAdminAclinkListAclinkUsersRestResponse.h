//
// EvhAdminAclinkListAclinkUsersRestResponse.h
// generated at 2016-04-07 17:57:43 
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
