//
// EvhTechparkRentalFindRentalSitesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalFindRentalSitesCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalFindRentalSitesRestResponse
//
@interface EvhTechparkRentalFindRentalSitesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalFindRentalSitesCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
