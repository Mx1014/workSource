//
// EvhRentalSiteItemDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteItemDTO
//
@interface EvhRentalSiteItemDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* itemName;

@property(nonatomic, copy) NSNumber* itemPrice;

@property(nonatomic, copy) NSNumber* counts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

