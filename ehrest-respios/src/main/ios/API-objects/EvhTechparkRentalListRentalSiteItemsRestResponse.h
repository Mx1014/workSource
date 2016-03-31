//
// EvhTechparkRentalListRentalSiteItemsRestResponse.h
// generated at 2016-03-31 15:43:24 
//
#import "RestResponseBase.h"
#import "EvhListRentalSiteItemsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalListRentalSiteItemsRestResponse
//
@interface EvhTechparkRentalListRentalSiteItemsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListRentalSiteItemsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
