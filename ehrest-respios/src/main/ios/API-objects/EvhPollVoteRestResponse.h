//
// EvhPollVoteRestResponse.h
// generated at 2016-03-30 10:13:09 
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
