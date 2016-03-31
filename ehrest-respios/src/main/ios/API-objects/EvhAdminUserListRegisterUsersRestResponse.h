//
// EvhAdminUserListRegisterUsersRestResponse.h
// generated at 2016-03-31 19:08:54 
//
#import "RestResponseBase.h"
#import "EvhListRegisterUsersResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUserListRegisterUsersRestResponse
//
@interface EvhAdminUserListRegisterUsersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListRegisterUsersResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
