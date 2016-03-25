//
// EvhConfGetVideoConfAccountPreferentialRuleRestResponse.h
// generated at 2016-03-25 17:08:12 
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
