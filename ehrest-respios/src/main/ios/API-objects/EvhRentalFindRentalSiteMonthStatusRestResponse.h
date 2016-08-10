//
// EvhRentalFindRentalSiteMonthStatusRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhFindRentalSiteMonthStatusCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindRentalSiteMonthStatusRestResponse
//
@interface EvhRentalFindRentalSiteMonthStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindRentalSiteMonthStatusCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
