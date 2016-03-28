//
// EvhUserFetchPastToRecentMessagesRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"
#import "EvhFetchMessageCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserFetchPastToRecentMessagesRestResponse
//
@interface EvhUserFetchPastToRecentMessagesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFetchMessageCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
