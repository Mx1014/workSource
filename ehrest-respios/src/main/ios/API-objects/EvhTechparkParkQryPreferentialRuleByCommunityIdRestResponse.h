//
// EvhTechparkParkQryPreferentialRuleByCommunityIdRestResponse.h
// generated at 2016-04-05 13:45:27 
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
