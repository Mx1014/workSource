//
// EvhAdminUserListUsersWithAddrRestResponse.h
// generated at 2016-03-31 13:49:15 
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
