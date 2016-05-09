//
// EvhConfListConfAccountSaleRulesRestResponse.h
// generated at 2016-04-29 18:56:03 
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
