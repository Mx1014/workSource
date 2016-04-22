//
// EvhTechparkRentalFindRentalSiteRulesRestResponse.h
// generated at 2016-04-22 13:56:51 
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
