//
// EvhTechparkRentalFindRentalSiteRulesRestResponse.h
// generated at 2016-03-31 20:15:34 
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
