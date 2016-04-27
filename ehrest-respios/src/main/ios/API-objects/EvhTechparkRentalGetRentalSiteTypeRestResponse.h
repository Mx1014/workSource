//
// EvhTechparkRentalGetRentalSiteTypeRestResponse.h
// generated at 2016-04-26 18:22:57 
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
