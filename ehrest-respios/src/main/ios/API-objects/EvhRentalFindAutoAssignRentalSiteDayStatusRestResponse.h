//
// EvhRentalFindAutoAssignRentalSiteDayStatusRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhFindAutoAssignRentalSiteDayStatusResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindAutoAssignRentalSiteDayStatusRestResponse
//
@interface EvhRentalFindAutoAssignRentalSiteDayStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindAutoAssignRentalSiteDayStatusResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
