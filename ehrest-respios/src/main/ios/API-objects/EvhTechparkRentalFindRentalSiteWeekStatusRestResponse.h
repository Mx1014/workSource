//
// EvhTechparkRentalFindRentalSiteWeekStatusRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalFindRentalSiteWeekStatusCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalFindRentalSiteWeekStatusRestResponse
//
@interface EvhTechparkRentalFindRentalSiteWeekStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalFindRentalSiteWeekStatusCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
