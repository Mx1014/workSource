//
// EvhAdminUserSearchUsersWithAddrRestResponse.h
// generated at 2016-03-28 15:56:09 
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
