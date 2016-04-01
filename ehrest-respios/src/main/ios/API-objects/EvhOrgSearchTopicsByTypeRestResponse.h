//
// EvhOrgSearchTopicsByTypeRestResponse.h
// generated at 2016-04-01 15:40:24 
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
