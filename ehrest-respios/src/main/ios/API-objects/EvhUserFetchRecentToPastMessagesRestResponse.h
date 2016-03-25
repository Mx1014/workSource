//
// EvhUserFetchRecentToPastMessagesRestResponse.h
// generated at 2016-03-25 09:26:45 
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
