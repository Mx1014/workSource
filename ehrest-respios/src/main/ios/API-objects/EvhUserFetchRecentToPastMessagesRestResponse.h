//
// EvhUserFetchRecentToPastMessagesRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhFetchMessageCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserFetchRecentToPastMessagesRestResponse
//
@interface EvhUserFetchRecentToPastMessagesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFetchMessageCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
