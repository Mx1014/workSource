//
// EvhAdminUserSearchUsersWithAddrRestResponse.h
// generated at 2016-04-22 13:56:50 
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
