//
// EvhAdminUserListUsersWithAddrRestResponse.h
// generated at 2016-04-07 14:16:31 
//
#import "RestResponseBase.h"
#import "EvhListUsersWithAddrResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUserListUsersWithAddrRestResponse
//
@interface EvhAdminUserListUsersWithAddrRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListUsersWithAddrResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
