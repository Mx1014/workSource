//
// EvhConfSetVideoConfAccountPreferentialRuleRestResponse.h
// generated at 2016-04-08 20:09:23 
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
