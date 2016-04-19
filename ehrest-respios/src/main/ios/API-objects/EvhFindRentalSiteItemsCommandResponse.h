//
// EvhFindRentalSiteItemsCommandResponse.h
// generated at 2016-04-19 14:25:56 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteItemsCommandResponse
//
@interface EvhFindRentalSiteItemsCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

