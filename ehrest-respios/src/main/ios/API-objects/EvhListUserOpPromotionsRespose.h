//
// EvhListUserOpPromotionsRespose.h
// generated at 2016-03-31 15:43:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOpPromotionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserOpPromotionsRespose
//
@interface EvhListUserOpPromotionsRespose
    : NSObject<EvhJsonSerializable>


// item type EvhOpPromotionDTO*
@property(nonatomic, strong) NSMutableArray* promotions;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

