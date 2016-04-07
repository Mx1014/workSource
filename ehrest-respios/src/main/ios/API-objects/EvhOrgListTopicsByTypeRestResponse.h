//
// EvhOrgListTopicsByTypeRestResponse.h
// generated at 2016-04-07 10:47:33 
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
