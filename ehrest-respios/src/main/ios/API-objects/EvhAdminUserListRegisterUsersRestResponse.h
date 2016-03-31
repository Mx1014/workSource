//
// EvhAdminUserListRegisterUsersRestResponse.h
// generated at 2016-03-31 13:49:15 
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
