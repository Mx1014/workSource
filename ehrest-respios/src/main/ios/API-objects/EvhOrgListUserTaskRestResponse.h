//
// EvhOrgListUserTaskRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhListTopicsByTypeCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListUserTaskRestResponse
//
@interface EvhOrgListUserTaskRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListTopicsByTypeCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
