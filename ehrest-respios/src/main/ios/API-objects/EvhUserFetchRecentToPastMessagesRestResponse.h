//
// EvhUserFetchRecentToPastMessagesRestResponse.h
// generated at 2016-04-19 14:25:58 
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
