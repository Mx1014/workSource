//
// EvhAdminUserListUsersWithAddrRestResponse.h
// generated at 2016-04-19 14:25:57 
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
