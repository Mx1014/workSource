//
// EvhOrgSearchTopicsByTypeRestResponse.h
// generated at 2016-03-31 20:15:33 
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
