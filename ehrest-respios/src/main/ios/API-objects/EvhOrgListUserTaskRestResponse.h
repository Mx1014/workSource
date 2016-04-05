//
// EvhOrgListUserTaskRestResponse.h
// generated at 2016-04-05 13:45:27 
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
