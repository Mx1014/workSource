//
// EvhAdminUserListUsersWithAddrRestResponse.h
// generated at 2016-04-07 10:47:33 
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
