//
// EvhTechparkRentalListRentalSiteItemsRestResponse.h
// generated at 2016-04-07 14:16:31 
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
