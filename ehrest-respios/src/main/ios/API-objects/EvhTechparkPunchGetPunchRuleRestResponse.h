//
// EvhTechparkPunchGetPunchRuleRestResponse.h
// generated at 2016-04-07 15:16:54 
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
