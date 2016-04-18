//
// EvhPollVoteRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhPollDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollVoteRestResponse
//
@interface EvhPollVoteRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPollDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
