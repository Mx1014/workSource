//
// EvhListRentalSiteItemsCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRentalSiteItemsCommandResponse
//
@interface EvhListRentalSiteItemsCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

