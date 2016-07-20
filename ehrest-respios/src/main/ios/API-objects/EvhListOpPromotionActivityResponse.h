//
// EvhListOpPromotionActivityResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOpPromotionActivityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOpPromotionActivityResponse
//
@interface EvhListOpPromotionActivityResponse
    : NSObject<EvhJsonSerializable>


// item type EvhOpPromotionActivityDTO*
@property(nonatomic, strong) NSMutableArray* promotions;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

