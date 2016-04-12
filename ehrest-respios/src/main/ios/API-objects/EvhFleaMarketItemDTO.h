//
// EvhFleaMarketItemDTO.h
// generated at 2016-04-12 19:00:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFleaMarketItemDTO
//
@interface EvhFleaMarketItemDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* barterFlag;

@property(nonatomic, copy) NSString* price;

@property(nonatomic, copy) NSNumber* closeFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

