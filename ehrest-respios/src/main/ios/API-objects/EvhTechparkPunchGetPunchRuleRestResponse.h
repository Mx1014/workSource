//
// EvhTechparkPunchGetPunchRuleRestResponse.h
// generated at 2016-04-26 18:22:57 
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
