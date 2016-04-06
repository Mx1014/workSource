//
// EvhPollShowResultRestResponse.h
// generated at 2016-04-06 19:10:44 
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
