//
// EvhGetItemListCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2SiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetItemListCommandResponse
//
@interface EvhGetItemListCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRentalv2SiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

