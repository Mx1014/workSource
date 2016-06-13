//
// EvhConfListConfAccountSaleRulesRestResponse.h
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
