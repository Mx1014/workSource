//
// EvhFindRentalSiteItemsAndAttachmentsResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2SiteItemDTO.h"
#import "EvhAttachmentConfigDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteItemsAndAttachmentsResponse
//
@interface EvhFindRentalSiteItemsAndAttachmentsResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalv2SiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

// item type EvhAttachmentConfigDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

