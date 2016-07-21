//
// EvhTechparkRentalFindRentalSitesStatusRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhFindRentalSitesStatusCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalFindRentalSitesStatusRestResponse
//
@interface EvhTechparkRentalFindRentalSitesStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindRentalSitesStatusCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
