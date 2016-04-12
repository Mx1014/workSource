//
// EvhTechparkRentalFindRentalSiteRulesRestResponse.h
// generated at 2016-04-08 20:09:24 
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
