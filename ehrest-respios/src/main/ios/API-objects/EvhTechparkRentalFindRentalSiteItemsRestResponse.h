//
// EvhTechparkRentalFindRentalSiteItemsRestResponse.h
// generated at 2016-04-22 13:56:51 
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
