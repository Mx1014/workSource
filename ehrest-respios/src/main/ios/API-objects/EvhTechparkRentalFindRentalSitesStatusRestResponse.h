//
// EvhTechparkRentalFindRentalSitesStatusRestResponse.h
// generated at 2016-03-30 10:13:09 
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
