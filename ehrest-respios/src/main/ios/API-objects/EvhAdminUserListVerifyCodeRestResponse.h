//
// EvhAdminUserListVerifyCodeRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhListVerfyCodeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUserListVerifyCodeRestResponse
//
@interface EvhAdminUserListVerifyCodeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListVerfyCodeResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
