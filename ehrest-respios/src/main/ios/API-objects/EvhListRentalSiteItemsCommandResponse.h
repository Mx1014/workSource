//
// EvhListRentalSiteItemsCommandResponse.h
// generated at 2016-04-26 18:22:54 
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

