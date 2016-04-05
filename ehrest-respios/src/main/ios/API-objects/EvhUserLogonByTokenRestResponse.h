//
// EvhUserLogonByTokenRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhLogonCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserLogonByTokenRestResponse
//
@interface EvhUserLogonByTokenRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhLogonCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
