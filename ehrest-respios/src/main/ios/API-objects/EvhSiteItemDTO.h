//
// EvhSiteItemDTO.h
// generated at 2016-04-05 13:45:25 
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

