//
// EvhTechparkRentalFindRentalSitesStatusRestResponse.h
// generated at 2016-04-05 13:45:27 
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
