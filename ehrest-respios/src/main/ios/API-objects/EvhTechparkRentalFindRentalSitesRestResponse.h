//
// EvhTechparkRentalFindRentalSitesRestResponse.h
// generated at 2016-03-31 10:18:21 
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
