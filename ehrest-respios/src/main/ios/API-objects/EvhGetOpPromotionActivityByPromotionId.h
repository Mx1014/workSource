//
// EvhGetOpPromotionActivityByPromotionId.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetOpPromotionActivityByPromotionId
//
@interface EvhGetOpPromotionActivityByPromotionId
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* promotionId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

