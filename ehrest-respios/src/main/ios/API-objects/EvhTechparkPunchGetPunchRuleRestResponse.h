//
// EvhTechparkPunchGetPunchRuleRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhGetPunchRuleCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkPunchGetPunchRuleRestResponse
//
@interface EvhTechparkPunchGetPunchRuleRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetPunchRuleCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
