//
// EvhPollVoteRestResponse.h
// generated at 2016-04-07 10:47:33 
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
