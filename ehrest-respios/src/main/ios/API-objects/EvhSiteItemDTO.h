//
// EvhSiteItemDTO.h
// generated at 2016-04-12 15:02:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSiteItemDTO
//
@interface EvhSiteItemDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* itemName;

@property(nonatomic, copy) NSNumber* itemPrice;

@property(nonatomic, copy) NSNumber* counts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

