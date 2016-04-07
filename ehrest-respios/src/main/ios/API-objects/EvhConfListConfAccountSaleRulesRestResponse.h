//
// EvhConfListConfAccountSaleRulesRestResponse.h
// generated at 2016-04-07 14:16:31 
//
#import "RestResponseBase.h"
#import "EvhListVideoConfAccountRuleResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListConfAccountSaleRulesRestResponse
//
@interface EvhConfListConfAccountSaleRulesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListVideoConfAccountRuleResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
