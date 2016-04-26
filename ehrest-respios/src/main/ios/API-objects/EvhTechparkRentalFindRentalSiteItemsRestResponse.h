//
// EvhTechparkRentalFindRentalSiteItemsRestResponse.h
// generated at 2016-04-26 18:22:57 
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
