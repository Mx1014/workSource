//
// EvhFindRentalSiteItemsAndAttachmentsResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSiteItemDTO.h"
#import "EvhAttachmentConfigDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteItemsAndAttachmentsResponse
//
@interface EvhFindRentalSiteItemsAndAttachmentsResponse
    : NSObject<EvhJsonSerializable>


// item type EvhSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

// item type EvhAttachmentConfigDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

