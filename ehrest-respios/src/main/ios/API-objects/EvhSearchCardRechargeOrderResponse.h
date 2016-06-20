//
// EvhSearchCardRechargeOrderResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCardRechargeOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchCardRechargeOrderResponse
//
@interface EvhSearchCardRechargeOrderResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhCardRechargeOrderDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

