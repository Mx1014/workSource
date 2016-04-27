//
// EvhUserVerifyAndLogonRestResponse.h
// generated at 2016-04-26 18:22:57 
//
#import "RestResponseBase.h"
#import "EvhLogonCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserVerifyAndLogonRestResponse
//
@interface EvhUserVerifyAndLogonRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhLogonCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
