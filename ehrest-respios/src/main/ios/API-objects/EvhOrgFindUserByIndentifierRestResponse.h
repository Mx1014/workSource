//
// EvhOrgFindUserByIndentifierRestResponse.h
// generated at 2016-04-19 14:25:58 
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
