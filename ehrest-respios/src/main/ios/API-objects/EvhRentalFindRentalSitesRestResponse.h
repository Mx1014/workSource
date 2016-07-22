//
// EvhRentalFindRentalSitesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhFindRentalSitesCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindRentalSitesRestResponse
//
@interface EvhRentalFindRentalSitesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindRentalSitesCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
