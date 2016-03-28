//
// EvhConfSetVideoConfAccountPreferentialRuleRestResponse.h
// generated at 2016-03-25 19:05:21 
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
