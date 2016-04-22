//
// EvhAdminOrgFindUserByIndentifierRestResponse.h
// generated at 2016-04-22 13:56:49 
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
