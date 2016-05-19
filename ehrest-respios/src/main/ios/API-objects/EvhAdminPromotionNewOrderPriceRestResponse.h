//
// EvhAdminPromotionNewOrderPriceRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhOpPromotionOrderRangeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminPromotionNewOrderPriceRestResponse
//
@interface EvhAdminPromotionNewOrderPriceRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhOpPromotionOrderRangeCommand* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
