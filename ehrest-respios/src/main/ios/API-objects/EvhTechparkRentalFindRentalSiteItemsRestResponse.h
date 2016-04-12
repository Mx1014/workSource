//
// EvhTechparkRentalFindRentalSiteItemsRestResponse.h
// generated at 2016-04-08 20:09:24 
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
