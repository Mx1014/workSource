//
// EvhRentalFindAutoAssignRentalSiteWeekStatusRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhFindAutoAssignRentalSiteWeekStatusResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindAutoAssignRentalSiteWeekStatusRestResponse
//
@interface EvhRentalFindAutoAssignRentalSiteWeekStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindAutoAssignRentalSiteWeekStatusResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
