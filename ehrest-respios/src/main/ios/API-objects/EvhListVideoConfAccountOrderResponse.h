//
// EvhListVideoConfAccountOrderResponse.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhConfOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVideoConfAccountOrderResponse
//
@interface EvhListVideoConfAccountOrderResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhConfOrderDTO*
@property(nonatomic, strong) NSMutableArray* confOrders;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

