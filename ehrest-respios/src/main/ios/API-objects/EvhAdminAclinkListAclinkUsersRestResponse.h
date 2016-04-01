//
// EvhAdminAclinkListAclinkUsersRestResponse.h
// generated at 2016-03-31 20:15:33 
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
