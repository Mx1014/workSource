//
// EvhPollShowResultRestResponse.h
// generated at 2016-03-30 10:13:09 
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
