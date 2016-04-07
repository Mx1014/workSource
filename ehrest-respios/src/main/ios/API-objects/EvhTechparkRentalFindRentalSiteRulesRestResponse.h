//
// EvhTechparkRentalFindRentalSiteRulesRestResponse.h
// generated at 2016-04-07 15:16:54 
//
#import "RestResponseBase.h"
#import "EvhFindRentalSiteRulesCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalFindRentalSiteRulesRestResponse
//
@interface EvhTechparkRentalFindRentalSiteRulesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindRentalSiteRulesCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
