//
// EvhFindRentalSiteItemsCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteItemsCommand
//
@interface EvhFindRentalSiteItemsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* rentalSiteRuleIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

