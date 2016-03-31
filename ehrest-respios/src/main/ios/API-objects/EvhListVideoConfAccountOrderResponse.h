//
// EvhListVideoConfAccountOrderResponse.h
// generated at 2016-03-31 13:49:14 
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

