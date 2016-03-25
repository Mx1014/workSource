//
// EvhAdminUserListRegisterUsersRestResponse.h
// generated at 2016-03-25 11:43:34 
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
