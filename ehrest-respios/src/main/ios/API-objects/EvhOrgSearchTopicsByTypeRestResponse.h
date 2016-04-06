//
// EvhOrgSearchTopicsByTypeRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhSearchTopicsByTypeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgSearchTopicsByTypeRestResponse
//
@interface EvhOrgSearchTopicsByTypeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSearchTopicsByTypeResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
