//
// EvhOpPromotionRangePriceData.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionRangePriceData
//
@interface EvhOpPromotionRangePriceData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* from;

@property(nonatomic, copy) NSNumber* to;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

