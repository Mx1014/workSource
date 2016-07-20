//
// EvhGetRefundOrderListResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRefundOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetRefundOrderListResponse
//
@interface EvhGetRefundOrderListResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhRefundOrderDTO*
@property(nonatomic, strong) NSMutableArray* refundOrders;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

