//
// EvhRentalFindRentalSiteWeekStatusRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalv2FindRentalSiteWeekStatusCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindRentalSiteWeekStatusRestResponse
//
@interface EvhRentalFindRentalSiteWeekStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalv2FindRentalSiteWeekStatusCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
