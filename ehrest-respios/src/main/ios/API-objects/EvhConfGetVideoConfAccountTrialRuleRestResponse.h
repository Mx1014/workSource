//
// EvhConfGetVideoConfAccountTrialRuleRestResponse.h
// generated at 2016-04-19 12:41:55 
//
#import "RestResponseBase.h"
#import "EvhVideoConfAccountTrialRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfGetVideoConfAccountTrialRuleRestResponse
//
@interface EvhConfGetVideoConfAccountTrialRuleRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhVideoConfAccountTrialRuleDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
