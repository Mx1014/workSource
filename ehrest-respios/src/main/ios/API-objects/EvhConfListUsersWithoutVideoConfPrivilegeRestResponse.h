//
// EvhConfListUsersWithoutVideoConfPrivilegeRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListUsersWithoutVideoConfPrivilegeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListUsersWithoutVideoConfPrivilegeRestResponse
//
@interface EvhConfListUsersWithoutVideoConfPrivilegeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListUsersWithoutVideoConfPrivilegeResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
