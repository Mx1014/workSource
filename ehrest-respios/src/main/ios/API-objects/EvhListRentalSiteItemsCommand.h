//
// EvhListRentalSiteItemsCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRentalSiteItemsCommand
//
@interface EvhListRentalSiteItemsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* enterpriseCommunityId;

@property(nonatomic, copy) NSString* siteType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

