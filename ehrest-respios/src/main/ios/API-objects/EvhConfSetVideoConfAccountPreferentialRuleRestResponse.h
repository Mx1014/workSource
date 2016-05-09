//
// EvhConfSetVideoConfAccountPreferentialRuleRestResponse.h
// generated at 2016-04-29 18:56:03 
//
#import "RestResponseBase.h"
#import "EvhVideoConfAccountPreferentialRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfSetVideoConfAccountPreferentialRuleRestResponse
//
@interface EvhConfSetVideoConfAccountPreferentialRuleRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhVideoConfAccountPreferentialRuleDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
