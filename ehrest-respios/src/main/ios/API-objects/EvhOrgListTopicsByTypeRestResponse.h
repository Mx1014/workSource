//
// EvhOrgListTopicsByTypeRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhListTopicsByTypeCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListTopicsByTypeRestResponse
//
@interface EvhOrgListTopicsByTypeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListTopicsByTypeCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
