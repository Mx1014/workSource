//
// EvhAdminUserSearchUsersWithAddrRestResponse.h
// generated at 2016-04-19 13:40:01 
//
#import "RestResponseBase.h"
#import "EvhUsersWithAddrResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUserSearchUsersWithAddrRestResponse
//
@interface EvhAdminUserSearchUsersWithAddrRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUsersWithAddrResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
