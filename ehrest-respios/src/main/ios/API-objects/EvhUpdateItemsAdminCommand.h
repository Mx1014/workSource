//
// EvhUpdateItemsAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2SiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateItemsAdminCommand
//
@interface EvhUpdateItemsAdminCommand
    : NSObject<EvhJsonSerializable>


// item type EvhRentalv2SiteItemDTO*
@property(nonatomic, strong) NSMutableArray* itemDTOs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

