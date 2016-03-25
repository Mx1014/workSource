//
// EvhUserLogonByTokenRestResponse.h
// generated at 2016-03-25 17:08:13 
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
