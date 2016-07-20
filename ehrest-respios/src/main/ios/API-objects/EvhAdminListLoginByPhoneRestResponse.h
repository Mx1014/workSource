//
// EvhAdminListLoginByPhoneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhUserLoginResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminListLoginByPhoneRestResponse
//
@interface EvhAdminListLoginByPhoneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserLoginResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
