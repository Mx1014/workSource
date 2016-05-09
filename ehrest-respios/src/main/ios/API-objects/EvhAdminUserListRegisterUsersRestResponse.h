//
// EvhAdminUserListRegisterUsersRestResponse.h
// generated at 2016-04-29 18:56:03 
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
