//
// EvhTechparkRentalFindRentalSiteWeekStatusRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"
#import "EvhFindRentalSiteWeekStatusCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalFindRentalSiteWeekStatusRestResponse
//
@interface EvhTechparkRentalFindRentalSiteWeekStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindRentalSiteWeekStatusCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
