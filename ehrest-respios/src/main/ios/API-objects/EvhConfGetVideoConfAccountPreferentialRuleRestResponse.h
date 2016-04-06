//
// EvhConfGetVideoConfAccountPreferentialRuleRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhVideoConfAccountPreferentialRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfGetVideoConfAccountPreferentialRuleRestResponse
//
@interface EvhConfGetVideoConfAccountPreferentialRuleRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhVideoConfAccountPreferentialRuleDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
