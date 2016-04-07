//
// EvhTechparkRentalFindRentalSitesRestResponse.h
// generated at 2016-04-07 15:16:54 
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
