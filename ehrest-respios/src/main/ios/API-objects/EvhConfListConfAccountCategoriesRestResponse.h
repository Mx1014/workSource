//
// EvhConfListConfAccountCategoriesRestResponse.h
// generated at 2016-03-25 17:08:12 
//
#import "RestResponseBase.h"
#import "EvhListVideoConfAccountRuleResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListConfAccountCategoriesRestResponse
//
@interface EvhConfListConfAccountCategoriesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListVideoConfAccountRuleResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
