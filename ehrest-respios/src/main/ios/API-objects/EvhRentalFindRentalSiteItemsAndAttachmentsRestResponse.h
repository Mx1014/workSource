//
// EvhRentalFindRentalSiteItemsAndAttachmentsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhFindRentalSiteItemsAndAttachmentsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindRentalSiteItemsAndAttachmentsRestResponse
//
@interface EvhRentalFindRentalSiteItemsAndAttachmentsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFindRentalSiteItemsAndAttachmentsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
