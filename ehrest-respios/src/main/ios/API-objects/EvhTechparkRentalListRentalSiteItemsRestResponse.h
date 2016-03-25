//
// EvhTechparkRentalListRentalSiteItemsRestResponse.h
// generated at 2016-03-25 17:08:13 
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
