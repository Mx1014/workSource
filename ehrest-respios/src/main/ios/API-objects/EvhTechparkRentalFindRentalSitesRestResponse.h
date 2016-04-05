//
// EvhTechparkRentalFindRentalSitesRestResponse.h
// generated at 2016-04-05 13:45:27 
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
