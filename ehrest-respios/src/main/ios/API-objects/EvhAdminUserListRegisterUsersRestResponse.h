//
// EvhAdminUserListRegisterUsersRestResponse.h
// generated at 2016-04-07 17:03:18 
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
