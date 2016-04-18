//
// EvhAdminAclinkListAclinkUsersRestResponse.h
// generated at 2016-04-18 14:48:52 
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
