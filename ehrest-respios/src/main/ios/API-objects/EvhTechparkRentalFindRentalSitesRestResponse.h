//
// EvhTechparkRentalFindRentalSitesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhFindRentalSitesCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalFindRentalSitesRestResponse
//
@interface EvhTechparkRentalFindRentalSitesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindRentalSitesCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
