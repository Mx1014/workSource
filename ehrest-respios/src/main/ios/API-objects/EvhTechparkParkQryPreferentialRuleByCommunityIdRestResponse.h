//
// EvhTechparkParkQryPreferentialRuleByCommunityIdRestResponse.h
// generated at 2016-03-25 17:08:13 
//
#import "RestResponseBase.h"
#import "EvhPreferentialRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkParkQryPreferentialRuleByCommunityIdRestResponse
//
@interface EvhTechparkParkQryPreferentialRuleByCommunityIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPreferentialRulesDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
