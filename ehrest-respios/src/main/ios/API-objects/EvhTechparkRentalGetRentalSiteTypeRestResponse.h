//
// EvhTechparkRentalGetRentalSiteTypeRestResponse.h
// generated at 2016-03-31 13:49:15 
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
