//
// EvhTechparkRentalFindRentalSiteItemsRestResponse.h
// generated at 2016-03-31 20:15:34 
//
#import "RestResponseBase.h"
#import "EvhFindRentalSiteItemsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalFindRentalSiteItemsRestResponse
//
@interface EvhTechparkRentalFindRentalSiteItemsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindRentalSiteItemsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
