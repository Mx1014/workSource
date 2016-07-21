//
// EvhFindRentalSiteItemsCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteItemsCommandResponse
//
@interface EvhFindRentalSiteItemsCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

