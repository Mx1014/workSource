//
// EvhAdminOrgFindUserByIndentifierRestResponse.h
// generated at 2016-04-07 17:03:18 
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
