//
// EvhListRentalSiteItemsCommandResponse.h
// generated at 2016-04-06 19:59:47 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRentalSiteItemsCommandResponse
//
@interface EvhListRentalSiteItemsCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

