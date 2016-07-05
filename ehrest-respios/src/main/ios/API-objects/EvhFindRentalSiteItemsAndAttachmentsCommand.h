//
// EvhFindRentalSiteItemsAndAttachmentsCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteItemsAndAttachmentsCommand
//
@interface EvhFindRentalSiteItemsAndAttachmentsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* rentalSiteRuleIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

