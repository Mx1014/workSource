//
// EvhUserVerifyAndLogonByIdentifierRestResponse.h
// generated at 2016-04-07 14:16:32 
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
