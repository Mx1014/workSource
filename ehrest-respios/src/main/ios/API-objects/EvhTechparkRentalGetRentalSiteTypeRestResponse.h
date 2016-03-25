//
// EvhTechparkRentalGetRentalSiteTypeRestResponse.h
// generated at 2016-03-25 09:26:45 
//
#import "RestResponseBase.h"
#import "EvhGetRentalSiteTypeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalGetRentalSiteTypeRestResponse
//
@interface EvhTechparkRentalGetRentalSiteTypeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetRentalSiteTypeResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
