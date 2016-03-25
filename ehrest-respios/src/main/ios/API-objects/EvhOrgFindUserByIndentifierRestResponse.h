//
// EvhOrgFindUserByIndentifierRestResponse.h
// generated at 2016-03-25 17:08:13 
//
#import "RestResponseBase.h"
#import "EvhUserTokenCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgFindUserByIndentifierRestResponse
//
@interface EvhOrgFindUserByIndentifierRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserTokenCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
