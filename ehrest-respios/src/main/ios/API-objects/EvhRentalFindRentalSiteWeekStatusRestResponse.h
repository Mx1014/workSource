//
// EvhRentalFindRentalSiteWeekStatusRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhFindRentalSiteWeekStatusCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindRentalSiteWeekStatusRestResponse
//
@interface EvhRentalFindRentalSiteWeekStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindRentalSiteWeekStatusCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
