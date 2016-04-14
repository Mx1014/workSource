//
// EvhUserVerifyAndLogonByIdentifierRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhLogonCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserVerifyAndLogonByIdentifierRestResponse
//
@interface EvhUserVerifyAndLogonByIdentifierRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhLogonCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
