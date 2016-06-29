//
// EvhGetItemListCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetItemListCommandResponse
//
@interface EvhGetItemListCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

