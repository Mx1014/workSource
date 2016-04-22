//
// EvhAdminUserListVerifyCodeRestResponse.h
// generated at 2016-04-22 13:56:50 
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
