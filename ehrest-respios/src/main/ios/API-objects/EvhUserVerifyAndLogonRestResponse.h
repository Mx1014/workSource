//
// EvhUserVerifyAndLogonRestResponse.h
// generated at 2016-04-18 14:48:53 
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
