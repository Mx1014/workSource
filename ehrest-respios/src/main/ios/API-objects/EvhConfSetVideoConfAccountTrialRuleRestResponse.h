//
// EvhConfSetVideoConfAccountTrialRuleRestResponse.h
// generated at 2016-04-07 17:57:43 
//
#import "RestResponseBase.h"
#import "EvhVideoConfAccountTrialRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfSetVideoConfAccountTrialRuleRestResponse
//
@interface EvhConfSetVideoConfAccountTrialRuleRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhVideoConfAccountTrialRuleDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
