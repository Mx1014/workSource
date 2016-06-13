//
// EvhAdminOrgFindUserByIndentifierRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhUserTokenCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgFindUserByIndentifierRestResponse
//
@interface EvhAdminOrgFindUserByIndentifierRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserTokenCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
