//
// EvhAddRentalSiteItemsCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddRentalSiteItemsCommand
//
@interface EvhAddRentalSiteItemsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseCommunityId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSString* itemName;

@property(nonatomic, copy) NSNumber* itemPrice;

@property(nonatomic, copy) NSNumber* counts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

