//
// EvhTechparkRentalGetRentalSiteTypeRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalGetRentalSiteTypeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalGetRentalSiteTypeRestResponse
//
@interface EvhTechparkRentalGetRentalSiteTypeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalGetRentalSiteTypeResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
