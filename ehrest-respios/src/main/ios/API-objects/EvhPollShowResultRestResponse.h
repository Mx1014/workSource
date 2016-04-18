//
// EvhPollShowResultRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhPollShowResultResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollShowResultRestResponse
//
@interface EvhPollShowResultRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPollShowResultResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
