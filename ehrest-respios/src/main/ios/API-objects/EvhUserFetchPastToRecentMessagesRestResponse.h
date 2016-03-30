//
// EvhUserFetchPastToRecentMessagesRestResponse.h
// generated at 2016-03-30 10:13:10 
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
