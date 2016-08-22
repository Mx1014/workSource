//
// EvhRentalFindRentalSitesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalv2FindRentalSitesCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindRentalSitesRestResponse
//
@interface EvhRentalFindRentalSitesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalv2FindRentalSitesCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
